package com.smallsoho.mcplugin.image;

public class Config {

    public float maxSize = 1024 * 1024;
    public boolean isCheck = true;
    public boolean isCompress = true;
    public boolean isWebpConvert = true;
    public boolean isJPGConvert = true;
    public boolean enableWhenDebug = true;
    public boolean isCheckSize = true;
    public int maxWidth = 500;
    public int maxHeight = 500;
    public String[] whiteList = new String[]{};

    public void maxSize(float maxSize) {
        this.maxSize = maxSize;
    }

    public void isCheck(boolean check) {
        isCheck = check;
    }

    public void isCompress(boolean compress) {
        isCompress = compress;
    }

    public void isWebpConvert(boolean webpConvert) {
        isWebpConvert = webpConvert;
    }

    public void isJPGConvert(boolean JPGConvert) {
        isJPGConvert = JPGConvert;
    }

    public void enableWhenDebug(boolean enableWhenDebug) {
        this.enableWhenDebug = enableWhenDebug;
    }

    public void isCheckSize(boolean checkSize) {
        isCheckSize = checkSize;
    }

    public void maxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void maxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void whiteList(String[] whiteList) {
        this.whiteList = whiteList;
    }
}
