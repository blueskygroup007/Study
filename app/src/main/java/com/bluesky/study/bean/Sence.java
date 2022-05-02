package com.bluesky.study.bean;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * @author BlueSky
 * @date 2022/3/26
 * Description: 场景类:图片,视频,动图.由名称,路径,缩略图等组成.后续可能会加播放相关参数.
 */
public class Sence implements Serializable {

    String name;
    String path;

    public Sence(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sence sence = (Sence) o;
        return Objects.equal(name, sence.name) && Objects.equal(path, sence.path);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, path);
    }
}
