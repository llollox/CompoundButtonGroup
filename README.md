# CompoundButtonGroup

![Alt text](https://img.shields.io/badge/license-MIT-green.svg?style=flat)

An Android library to easily implement compound buttons

## Installation

#### Gradle
Add Gradle dependency:

```groovy
dependencies {
    compile 'us.belka:androidtoggleswitch:1.2.2'
}
```

#### Maven
```xml
<dependency>
  <groupId>us.belka</groupId>
  <artifactId>androidtoggleswitch</artifactId>
  <version>1.2.2</version>
  <type>pom</type>
</dependency>
```

## Usage

#### Check Box

![Sample of check_box](docs/screen/check_box.gif)

```xml
<com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:entries="@array/planets"
    app:compoundType="check_box"/>
```

#### Check Box Grid (2 Cols, Label After)

![Sample of check_box](docs/screen/check_box_2cols_label_after.gif)

```xml
<com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:entries="@array/planets"
    app:compoundType="check_box"
    app:numCols="2"
    app:labelOrder="after"/>
```


#### Radio

![Sample of radio](docs/screen/radio.gif)

```xml
<com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:entries="@array/planets"
    app:compoundType="radio"
    app:labelOrder="after"/>
```

#### Radio Grid (3 Cols)

![Sample of check_box](docs/screen/radio_3cols.gif)

```xml
<com.llollox.androidprojects.compoundbuttongroup.CompoundButtonGroup
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:entries="@array/planets"
    app:compoundType="radio"
    app:numCols="3"
    app:labelOrder="after"/>
```

## Getters and Setters

* `void setEntries(List<String> entries)` Set the entries for the compound button group.

```java
List<String> entries = new ArrayList<String>(){{add("Mars"); add("Mercury"); add("Earth");}};
compoundButtonGroup.setEntries(entries);
```

* `List<Integer> getCheckedPositions()` Returns the current checked positions

```java
List<Integer> positions = compoundButtonGroup.getCheckedPositions();
```

* `void setCheckedPosition(int position)` Checks the button at the position passed as argument. Typically to be used with `radio` buttons.

```java
int position = 3;
compoundButtonGroup.setCheckedPosition(position);
```

* `void setCheckedPositions(List<Integer> positions)` Checks all the buttons at the positions passed as argument. Typically to be used with `check box` buttons.

```java
List<Integer> positions = new ArrayList<Integer>(){{add(2); add(4);}};
compoundButtonGroup.setCheckedPositions(positions);
```


## Listeners

##### OnButtonSelectedListener

```java
compoundButtonGroup.setOnButtonSelectedListener(new CompoundButtonGroup.OnButtonSelectedListener() {
    @Override
    public void onButtonSelected(int position, boolean isChecked) {
        // Your code
    }
});
```

## Customization

It is possible to customize the buttons applying the following options:


| Option Name      				| Format                 | Description                              |
| ---------------- 				| ---------------------- | -----------------------------            |
| app:compoundType 	  | `check_box` or `radio` | The type of the compound button. By default is `check_box`.                 |
| app:entries 				| `array`  	        | String array of the entries of the compound button group.                 |
| app:labelOrder      | `before` or `after`    | This determines if the label is before or after the compound button. By default is `before`.    |
| app:numCols       | `int`               | Setting this parameter the compound buttons are shown as a grid and it indicates the number of cols of the grid.  |
