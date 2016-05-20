package com.tf.truefeeling.model;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.tf.truefeeling.R;
import com.tf.truefeeling.util.EncryptCoder;
import com.tf.truefeeling.util.FileUtils;
import com.tf.truefeeling.util.Log;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by pengfei.zhong on 1/13/16.
 */
public class UserIconLoader  {
    private static final String TAG = "UserIcon";
    private static final String TAG_DEFAULT_M ="m";
    private static final String TAG_DEFAULT_F ="f";
    private static final String TAG_DEFAULT_CUSTOM ="c";
    private static final String TAG_DEFAULT_NO_SET ="n";



    public interface UserLoadListener{
        void onIconReady(Drawable icon);
    }

    private static final  String USER_ICON  = "user-icon";
    private ImageLoader mImageLoader;
    private UserLoadListener mLoadListener;
    private IconViewTarget mIconViewTarget;
    private CircleTransform mCircleTransform;
    private Context mContext;

    public UserIconLoader(ImageLoader loader,Context context){
        mImageLoader = loader;
        this.mContext = context;
        mCircleTransform = new CircleTransform(mContext);
    }

    public void setLoadListener(UserLoadListener listener){
        this.mLoadListener = listener;
    }
    private void loadIcon(String url, int placeHolder, ImageView view, boolean needTransform, Drawable defaultIcon){
        if(TextUtils.isEmpty(url)){
            Log.d(TAG," url is empty! " + (url==null? "EMPTY":url));
            mImageLoader.loadImage("", view, placeHolder, null);
            return;
        }
        String local = getLocalPath(url);
        if(TextUtils.isEmpty(local)){
            Log.d(TAG, " local is empty! " + url);
            if(defaultIcon == null) {
                mImageLoader.loadImage(url, view, placeHolder, needTransform ? mCircleTransform : null);
            }else {
                mImageLoader.loadImage(url, view, defaultIcon, needTransform ? mCircleTransform : null);
            }
            return;
        }
        File  file = new File(local);
        boolean localUrl =  file.exists() && file.isFile();

        if(localUrl){
            if(defaultIcon == null){
                mImageLoader.loadLocalUri(Uri.parse("file://" + local), view, placeHolder, needTransform ? mCircleTransform : null);
            }else{
                mImageLoader.loadLocalUri(Uri.parse("file://" + local), view, defaultIcon, needTransform ? mCircleTransform : null);
            }
            Log.d(TAG, " load local url, " + localUrl);
        } else{
            Log.d(TAG, " load internal url, " + url);
            if(defaultIcon == null){
                mImageLoader.loadImage(url, view, placeHolder, needTransform ? mCircleTransform : null);
            }else{
                mImageLoader.loadImage(url, view, defaultIcon, needTransform ? mCircleTransform : null);
            }
        }
    }
    private  String getCurrentTag(TFUserInfo userInfo){
        if(userInfo == null){
            return TAG_DEFAULT_M;
        }
        String avatar = userInfo.getAvatar();
        if(TextUtils.isEmpty(avatar)){
            if(userInfo.isFemale()) {
                return TAG_DEFAULT_F;
            }else{
                return TAG_DEFAULT_M;
            }
        }
        return TAG_DEFAULT_CUSTOM;

    }

    public void loadIcon(TFUserInfo info, ImageView view, boolean needTransform, boolean needSetDefaultIcon){
        int defaultIcon = R.drawable.ic_user_default_male;
        if(info == null){
            view.setTag(R.id.icon_tag_id, TAG_DEFAULT_M);
            loadIcon("", defaultIcon, view, needTransform, null);
            return;
        }
        boolean isFemale = info.isFemale();

        Object iconTagObj = view.getTag(R.id.icon_tag_id);
        String oldIconTag = iconTagObj==null? TAG_DEFAULT_NO_SET :iconTagObj.toString();
        String currTag =   getCurrentTag(info);
        Log.d(TAG," oldTag:" + oldIconTag+", current:" + currTag);
        if(currTag.equals(TAG_DEFAULT_CUSTOM)){
//            if(!oldIconTag.equals(TAG_DEFAULT_CUSTOM)){
                defaultIcon = isFemale?R.drawable.ic_user_default_female:R.drawable.ic_user_default_male;
                Log.d(TAG," curr is CUST, old is not cust, set default icon");
//            }else{
//                defaultIcon = 0;
//                Log.d(TAG," previous icon is cust, current is cust,no set default icon");
//            }
        }else{  //current is no set avatar url.
            defaultIcon = isFemale?R.drawable.ic_user_default_female:R.drawable.ic_user_default_male;
            Log.d(TAG," set default by gender:" + info.getGender());
        }
        view.setTag(R.id.icon_tag_id,currTag);
        loadIcon(info.getAvatar(), defaultIcon, view, needTransform, null);
    }

    public void loadIcon(TFUserInfo info, ImageView view, boolean needTransForm){
           loadIcon(info,view,needTransForm,true);
    }

/*    private RequestQueue requestQueue;
    private void addRequest(Request<?> request) {
        if (requestQueue == null) {
            requestQueue = AsyncHttp.newRequestQueue();
        }
        requestQueue.addRequest(request);
    }*/
    public String getLocalPath(String url){
        if(TextUtils.isEmpty(url)){
            return "";
        }
        String local = EncryptCoder.encryptMD5(url, false);
        StringBuilder sb = new StringBuilder(20);
        sb.append(FileUtils.getAvatarDirectory());
        sb.append(local);
        sb.append(".jpg");
        Log.d(TAG, "getLocal Path: " + sb.toString());
        return sb.toString();


    }

    public class IconViewTarget extends ViewTarget{
        public IconViewTarget(ImageView view){
            super(view);
        }

        @Override
        public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
            ((ImageView)getView()).setImageDrawable((Drawable) resource);
        }
    }

    public static class CircleTransform extends BitmapTransformation {
        private Context mContext;
        public CircleTransform(Context context) {
            super(context);
            this.mContext = context;
        }

        @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
           int size = mContext.getResources().getDimensionPixelOffset(R.dimen.main_activity_user_icon_height);
            return circleCrop(pool, toTransform, size);
        }

        private static Bitmap circleCrop(BitmapPool pool, Bitmap source, int size) {
            if (source == null) return null;

//            int size =  //Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            // TODO this could be acquired from the pool too
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override public String getId() {
            return getClass().getName();
        }
    }

}
