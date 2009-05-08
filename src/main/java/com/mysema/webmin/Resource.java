/**
 * 
 */
package com.mysema.webmin;

/**
 * Resource provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class Resource {
    private final boolean forward;

    private boolean l10n;

    private String path;

    public Resource(String path, boolean forward, boolean l10n) {
        this.path = path;
        this.forward = forward;
        this.l10n = l10n;
    }

    public boolean equals(Object o) {
        return o instanceof Resource && ((Resource) o).path.equals(path);
    }

    public String getPath() {
        return path;
    }

    public void addPathPrefix(String basePath) {
        this.path = basePath + path;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isL10n() {
        return l10n;
    }

}