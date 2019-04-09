![GitHub Logo](https://img.shields.io/badge/version-1.0-orange.svg) ![GitHub Logo](https://img.shields.io/badge/minSdkVersion-21-blue.svg)


![GitHub Logo](http://i67.tinypic.com/mmq0py.png)

## Setup

### Gradle

Add in your root build.gradle at the end of repositories:

```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency

```gradle
dependencies {
    implementation 'com.github.Ftouzi:IndexedProgress:1.0'
}
```

### Maven

```gradle
<repositories>
	<repository>
	    <id>jitpack.io</id>
	    <url>https://jitpack.io</url>
	</repository>
</repositories>
```

Add the dependency

```gradle
<dependency>
    <groupId>com.github.Ftouzi</groupId>
    <artifactId>IndexedProgress</artifactId>
    <version>1.0</version>
</dependency>
```

## How does this work ?

* Using only XML attributes :

```xml
<com.ftouzi.indexedprogress.IndexedProgress
            android:id="@+id/indexedProgress"
            android:layout_width="match_parent"
            android:layout_height="30dp"
            app:value="75"
            app:base_color="@color/colorPrimaryDark"
            app:progress_shader="@drawable/ic_lines"
            app:separator_color="@color/colorPrimary"
            app:value_color="@android:color/white" />
```

* Using simple setters :

```kotlin
 indexedProgress.setValue(12f)
 indexedProgress.setValueColor(Color.RED)

```
   

    
