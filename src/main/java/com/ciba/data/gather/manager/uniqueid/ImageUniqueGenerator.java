package com.ciba.data.gather.manager.uniqueid;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * 存放在图片媒体目录uniqueId文件 必须放在责任链最后
 *
 * @author parting_soul
 * @date 2019-09-19
 */
public class ImageUniqueGenerator extends BaseUniqueIdGenerator {

    public ImageUniqueGenerator(Context context) {
        super(context);
    }

    @Override
    protected Uri getMediaUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }


    @Override
    protected String getMediaFileSuffix() {
        return ".png";
    }

    @Override
    protected String getMediaFilePrefix() {
        return "5eb63bbbe01eeed093cb22bb8f5acdc3";
    }

    @Override
    protected File getUniqueFileDir() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    @Override
    protected String getUniqueFileMimeType() {
        return "image/png";
    }

    @Override
    protected String getBucketDisplayName() {
        return Environment.DIRECTORY_PICTURES;
    }


    @Override
    protected String getMediaRawData() {
        return "iVBORw0KGgoAAAANSUhEUgAAABkAAAASCAYAAACuLnWgAAAAAXNSR0IArs4c6QAAADhlWElmTU0AKgAAAAgAAYdpAAQAAAABAAAAGgAAAAAAAqACAAQAAAABAAAAGaADAAQAAAABAAAAEgAAAAAM64R1AAACx0lEQVQ4Eb1TS09aQRTmXh4FwuOStlyKQpGHlITWFpOmC7poE+KCBYmLblwRt6BhAT+A8AOMcQd/gZS0STekC+JCWrBNMXVTDbUlCkRQQSLIq+eYDLlFUJPGnuRkzsyc+b45L4p3M5GBGwN6H1QAKge9sVDXeCLwFKjqGr8rr8eR4G8f/ys4YR5FgqlBAglxEggEPI/Hc8/n8720WCxPJRIJQ1EU//z8/LRcLucTiURqbW1tB+wOecNd+dwN2EjwDPQOOUeCQCBgDIVCb4xG4yxN04J2u90QiURSuBMrlUrWbreb4K6xvr5+cHZ21idvyUoTA1aSIiHnjOd0OhVer9etVqvNQEAXCoWttyDNZrOGfkDYPDo62mcYRqZSqRDjknBJLHA7SBHxXF5efq7RaKywp3u9XieVSn0JBoNf8/n8NzjrVSqVXysrK++WlpZSu7u7LfKOuxIS7KIH3AtiOxyOFxDARVobjUa1VCqdxmIxZzweT0NqTra3t7fgvMvn80fV9wKGkOgI6PAql8vV5Oz4+PjAYDAwkMJX2Wy2kkwm3+dyud9QL/f8/LyW+A2vSIJ5xCEbJ+SH/WKxuG8ymSakUinjdrv1fr//k9VqZfV6/czc3Jxdp9P9VU8CiCRXEfDq9XoJnbvdLtS43ZmcnHwI7SuCaJ4sLi7qZ0EgVUKbzeZYWFgYmRHMNdZCiUCjxOVySQDYAsCUQqGQg7LQuiJIIzM9PX2XZVkr1kwoFErBpqPRaG4YByPB2Rgrq6urnw8PD3ewfaFNJ2A+0J8Si8UKSM8MRoGPcTAzmcz3UUAYCYY4GL5hp729vSb0f9VsNrMArISASLNgrVD7MDMnGxsbHyORyCa09KWpR5JHw8DcPcwGL51OV2u12k+tVtuXgcDvReBDtVqtOrT0D5jND+FweHPcnOBPXnNBb8Mmod8G9gDzv5D8AdaL4Y0DaK/jAAAAAElFTkSuQmCC";
    }

    @Override
    protected String getUniqueIdGenerateRuleKey() {
        return "image";
    }

    @Override
    protected boolean isGenerateWhenException() {
        return false;
    }

}
