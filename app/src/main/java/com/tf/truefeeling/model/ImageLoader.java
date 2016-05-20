package com.tf.truefeeling.model;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.tf.truefeeling.R;
import com.tf.truefeeling.util.Log;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.ImageView;

import java.util.HashMap;

/**
 * Created by shenggang on 15/3/9.
 */
public class ImageLoader {


    private static final String TAG = "ImageLoader";
    private static final boolean DEBUG = false;

    private static HashMap<Integer, Integer> defaultAvatarMap;
    public static final int[] AVATAR_RES_ARRAY = {R.drawable.default_avatar_blue,R.drawable.default_avatar_red,R.drawable.default_avatar_green,
            R.drawable.default_avatar_orange,R.drawable.default_avatar_purple};
    static {
        defaultAvatarMap = new HashMap<>();
        for(int i =0; i < AVATAR_RES_ARRAY.length; i++){
            defaultAvatarMap.put(i,AVATAR_RES_ARRAY[i]);
        }
    }
    private  RequestManager mRequestManager;



    private int mPlaceHolderResId = 0;
    private Context context;

    /**
     * If the cache image,mabye use in other screen, need use {@link Context#getApplicationContext()}
     * @param context
     */
    public ImageLoader(Context context) {
        this(context, 0);
    }

    /**
     * Construct an ImageLoader with a default placeholder drawable.
     * * If the cache image,mabye use in other screen, need use {@link Context#getApplicationContext()}
     */
    public ImageLoader(Context context, int placeHolderResId) {
        this.context = context;
        mPlaceHolderResId = placeHolderResId;
        mRequestManager =  Glide.with(this.context);
    }

    /**
     * Load an image from a url into an ImageView using the default placeholder
     * drawable if available.
     *
     * @param url       The web URL of an image.
     * @param imageView The target ImageView to load the image into.
     */
    public void loadImage(String url, ImageView imageView) {
        loadImage(url, imageView, null, null);
    }

    /**
     * Load an image from a url into an ImageView using the given placeholder drawable.
     *
     * @param url                The web URL of an image.
     * @param imageView          The target ImageView to load the image into.
     * @param placholderOverride A placeholder to use in place of the default placholder.
     */
    public void loadImage(String url, ImageView imageView, Drawable placholderOverride) {
        loadImage(url, imageView, placholderOverride,  null);
    }

    /**
     * Load an image from a url into an ImageView using the given placeholder drawable.
     *
     * @param url                The web URL of an image.
     * @param imageView          The target ImageView to load the image into.
     * @param placholderOverride A placeholder to use in place of the default placholder.
     */
    public void loadImage(String url, ImageView imageView, int placholderOverride) {
        loadImage(url, imageView, placholderOverride, null);
    }
    /**
     * Load an image from a url into an ImageView using the given placeholder drawable.
     *
     * @param url                The web URL of an image.
     * @param imageView          The target ImageView to load the image into.
     * @param placholderOverride A placeholder to use in place of the default placholder.
     */
    public void loadImage(String url, ImageView imageView, int placholderOverride, BitmapTransformation  transformation) {
        internalLoadImage(url, imageView, null, placholderOverride, transformation);
    }


    /**
     * Load an image from a url into an ImageView using the default placeholder
     * drawable if available.
     * @param url                 The web URL of an image.
     * @param imageView           The target ImageView to load the image into.
     * @param placeholderOverride A drawable to use as a placeholder for this specific image.
*                            If this parameter is present, {@link #mPlaceHolderResId}
     * @param transformation
     */
    public void loadImage(String url, final ImageView imageView,
                          Drawable placeholderOverride, BitmapTransformation transformation) {
             internalLoadImage(url, imageView, placeholderOverride, 0, transformation);
    }
    private void internalLoadImage(String url, ImageView imageView, Drawable placeHolder,
                                   int placeHolderResId, BitmapTransformation transformation){
        if(DEBUG){
            Log.d(TAG," loadImage with String, url:" + url +", view#"+ imageView.hashCode());
        }
        DrawableTypeRequest request = mRequestManager.load(url);
       DrawableRequestBuilder builder = request.diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate();
        if(transformation != null){
            builder.transform(transformation);
        }
        placeHolderAndLoadIcon(imageView, placeHolder, placeHolderResId, builder);
    }

    private void placeHolderAndLoadIcon(ImageView imageView, Drawable placeHolder, int placeHolderResId, DrawableRequestBuilder builder) {
        if(placeHolder != null){
            builder.placeholder(placeHolder);
        }else{
            if(placeHolderResId >0){
                builder.placeholder(placeHolderResId);
            }else if(mPlaceHolderResId > 0) {
                builder.placeholder(mPlaceHolderResId);
            }
        }
        builder.into(imageView);
    }

    /**
     * Load an image from a url into an ImageView using the default placeholder
     * drawable if available.
     *  @param url                 The web URL of an image.
     * @param imageView           The target ImageView to load the image into.
     * @param placeholderOverride A drawable to use as a placeholder for this specific image.
 *                            If this parameter is present, {@link #mPlaceHolderResId}
     * @param transformation
     */
    public void loadImage(String url, ImageView imageView,
                          int placeholderOverride, boolean crop, BitmapTransformation transformation) {
       internalLoadImage(url, imageView, null, placeholderOverride, null);
    }
    /**
     * new ViewTarget(imageView) {
    @Override
    public void onResourceReady(Object resource, GlideAnimation glideAnimation) {
    Log.d("JOE", " use ownViewTarget: " + resource.getClass().getName());
    ((ImageView) getView()).setImageDrawable(((GlideDrawable)resource).getCurrent());
    Log.d("JOE", " use ownViewTarget: " + resource.getClass().getName());
    }
    }
     */
    /**
     * Load an image from a url into an ImageView using the default placeholder
     * drawable if available.
     *
     * @param url                 The web URL of an image.
     * @param imageView           The target ImageView to load the image into.
     * @param placeholderOverride A drawable to use as a placeholder for this specific image.
     *                            If this parameter is present, {@link #mPlaceHolderResId}
     *                            if ignored for this request.
     */
//    public void loadImage(String url, ViewTarget imageView,
//                          int placeholderOverride, boolean crop) {
//        if(DEBUG){
//            Log.d(TAG," loadImage with String, url:" + url);
//        }
//        DrawableTypeRequest request =  mRequestManager.load(url);//beginImageLoad(url, crop);
//        request.diskCacheStrategy(DiskCacheStrategy.ALL);
//        if (placeholderOverride > 0) {
//            request.placeholder(placeholderOverride);
//        } else if (mPlaceHolderResId != -1) {
//            request.placeholder(mPlaceHolderResId);
//        }
//        request.into(imageView);
//    }


    public void loadLocalUri(Uri uri, ImageView imageView, Drawable placeHolderId){
        if(DEBUG) Log.d(TAG, "loadLocalImageThumnail-> uri:" + uri);
        internalLoadLocalUri(uri, imageView, placeHolderId, 0, null);
    }
    public void loadLocalUri(Uri uri, ImageView imageView, int placeHolderId){
        if(DEBUG) Log.d(TAG, "loadLocalImageThumnail-> uri:" + uri);
        internalLoadLocalUri(uri, imageView, null, placeHolderId, null);
    }
    public void loadLocalUri(Uri uri, ImageView imageView, int placeHolderId, BitmapTransformation transformation){
        if(DEBUG) Log.d(TAG, "loadLocalImageThumnail-> uri:" + uri);
        internalLoadLocalUri(uri, imageView, null, placeHolderId, transformation);
    }
    public void loadLocalUri(Uri uri, ImageView imageView, Drawable placeHolderId, BitmapTransformation transformation){
        if(DEBUG) Log.d(TAG, "loadLocalImageThumnail-> uri:" + uri);
        internalLoadLocalUri(uri, imageView, placeHolderId, 0, transformation);
    }
    private void internalLoadLocalUri(Uri uri, ImageView imageView, Drawable placeHolder, int placeHolderResId, BitmapTransformation transformation){
        DrawableRequestBuilder builder = null;
        if(isMediaStoreUri(uri)){
          builder = mRequestManager.loadFromMediaStore(uri).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
        }else{
            builder =  mRequestManager.load(uri).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL);
        }

        if(transformation != null){
            builder.transform(transformation);
        }

        placeHolderAndLoadIcon(imageView, placeHolder, placeHolderResId, builder);
    }

    public static int randomNumber() {
        return (int) (Math.random() * AVATAR_RES_ARRAY.length);
    }

    public Drawable getAvatar(int index, String nickname) {
                 return getAvatar(index,nickname,context);
    }
    /*public Drawable getAvatar(Contacts c) {
        int index = c.getOrSetupAvatarIndex();
        String nickName =  c.getNickname();
        if(TextUtils.isEmpty(nickName)){
            nickName = c.getEmail();
        }
        return getAvatar(index,nickName,context);
    }

    public static Drawable getAvatar(Object c, Context  context, boolean isMobileContact) {
        if (isMobileContact) {
            MobileContact contact = (MobileContact)c;
            int index = contact.getOrSetupAvatarIndex();
            String name =  contact.getMobileContactName();
            if(TextUtils.isEmpty(name)){
                name = contact.getMobilePhoneNumber();
            }
            return getAvatar(index,name,context);
        } else {
            return  getAvatar((Contacts)c, context);
        }
    }

    public static Drawable getAvatar(Contacts c,Context  context) {
        int index = c.getOrSetupAvatarIndex();
        String nickName = c.getDisplayName();
        if(TextUtils.isEmpty(nickName)){
            nickName = c.getEmail();
        }
        return getAvatar(index,nickName,context);
    }

    public static Drawable getAvatar(LiveLean c,Context  context) {
        String name = c.getDisplayName();
        if(TextUtils.isEmpty(name)){
            name = c.getCasterId();
        }
        return getAvatar(c.getAvatarIndex(),name,context);
    }*/

    public static  Drawable getAvatar(int index, String nickname, Context context) {
        if (!TextUtils.isEmpty(nickname)) {
            if (index >= 0 && index < AVATAR_RES_ARRAY.length) {
                String indexChar = null;
                int end = nickname.length();
                if(end > 1){
                    end = 1;
                }
                try {
                    indexChar = nickname.substring(0, end).toUpperCase();
                } catch (Exception e) {
                    e.printStackTrace();;
                    Log.e(TAG,"getAvatar exception: " + e.getMessage());
                    return context.getResources().getDrawable(defaultAvatarMap.get(index));
                }

                Bitmap bitmap = addTextToBitmap(indexChar, BitmapFactory.decodeResource(context.getResources(), defaultAvatarMap.get(index)), context,end);
                return new BitmapDrawable(bitmap);
            }else{
                Log.e(TAG,"getAvatar-> index error!, will return icon null!");
            }
        } else {
            if (index >= 0 && index < AVATAR_RES_ARRAY.length) {
                return context.getResources().getDrawable(defaultAvatarMap.get(index));
            }
        }
        Log.d(TAG, "index: " + index + ", use default avatar!");
        return context.getResources().getDrawable(R.drawable.ic_user_default_male);
    }

    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    private static Bitmap addTextToBitmap(String text, Bitmap bitmap, Context context, int end) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        paint.setTextSize(context.getResources().getDimension(R.dimen.default_icon_text_size));
        //paint.setTypeface(ReplaceFonts.SANS_BOLD);
        paint.setColor(context.getResources().getColor(R.color.white));
        paint.setTextAlign(Paint.Align.CENTER);
        final Rect src = new Rect(0, 0, width, height);
        final Rect dst = new Rect(0, 0, width, width);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawBitmap(bitmap, src, dst, paint);
        Rect textRect = new Rect();
        int len = text.length() >= 2 ? 2 : text.length();
        paint.getTextBounds(text, 0, len, textRect);
//        float charWidth =    textRect.width();
        float charHeight = textRect.height();
        float x=width/2;
//        float y=height/2;
//        float x =  (width-charWidth)/2;
        float y = height/2 +charHeight/2;
        canvas.drawText(text, x,  y, paint);
        return output;
    }
    public void onPause(){
        mRequestManager.pauseRequests();
        if(DEBUG) Log.d(TAG, "ImageLoader----> pauseRequests" );

    }
    public void onResume(){
       mRequestManager.resumeRequests();
         if(DEBUG) Log.d(TAG, "ImageLoader----> onResume");
    }

    public void onDestory(){
        mRequestManager.onDestroy();
        if(DEBUG) Log.d(TAG, "ImageLoader----> onResume");
    }
    public void onLowMemory(){
       mRequestManager.onLowMemory();

    }
    public void onTrimMemory(int level){
        mRequestManager.onTrimMemory(level);
    }

    public void clearMemoryCache(){
        Glide.get(context).clearMemory();
    }


    private static boolean isMediaStoreUri(Uri uri) {
        return uri != null
                && ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())
                && MediaStore.AUTHORITY.equals(uri.getAuthority());
    }
}
