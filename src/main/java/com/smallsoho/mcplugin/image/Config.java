package com.smallsoho.mcplugin.image;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public int maxSize = 1024 * 1024;
    public boolean isCheck = true;
    public boolean isCompress = true;
    public boolean isWebpConvert = true;
    public boolean isJPGConvert = true;
    public boolean enableWhenDebug = true;
    public boolean isCheckSize = true;
    public int maxWidth = 500;
    public int maxHeight = 500;
    public List<String> whiteList = new ArrayList<>();
}
