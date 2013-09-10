## webmin

Webmin is a set of filters and servlets for the compression of HTML, CSS and JavaScript content

### Usage

Declare the following elements in your web.xml

```
<servlet>
  <servlet-name>minifier</servlet-name>
  <servlet-class>com.mysema.webmin.MinifierServlet</servlet-class>
</servlet>
<servlet-mapping>
  <servlet-name>minifier</servlet-name>
  <url-pattern>/res/*</url-pattern>
</servlet-mapping>
```		

Create the file WEB-INF/minifier.xml with the minified resources

```
<minifier>
  <bundle path="/res/public.js">
    <max-age>86400</max-age>
    <resources>
      <resource>/js/jquery-1.6.1.js</resource>
      <resource>/js/jquery-noconflict.js</resource>
      <resource>/js/css_browser_selector.js</resource>
      <!-- ... -->
      </resources>
  </bundle>
  <bundle path="/res/styles.css" type="css">
    <max-age>86400</max-age>
    <resources>
      <resource>/css/layout.css</resource>
      <resource>/css/common.css</resource>
      <!-- ... ->
    </resources>
  </bundle>
</minifier>
```

The first bundle describes a minified js resource with three files and the bundle describes a css file with two files.
