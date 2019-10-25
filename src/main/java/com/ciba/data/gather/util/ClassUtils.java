package com.ciba.data.gather.util;

/**
 * @author parting_soul
 * @date 2019/2/2
 */
public class ClassUtils {
    /**
     * 库是否依赖
     *
     * @param className
     * @return
     */
    public static boolean isLibraryCompile(String className) {
        boolean isCompile;
        try {
            Class.forName(className);
            isCompile = true;
        } catch (Exception e) {
            isCompile = false;
        }
        return isCompile;
    }

}
