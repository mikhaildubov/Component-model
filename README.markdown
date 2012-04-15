# Component model with declarative type definition

<p>The project is devoted to constructing a component model using its declarative description. The goal is to build a set of connected <i>JavaBeans</i> components, called <i>scene graph</i>. These interactions between components are described with either <i>VRML</i> or <i>XML</i> language. Then, a special front-end visualizes the scene graph and allows the user to change the properties of the components and the ties between them. </p>
<p>Besides, the project shows the impossibility of efficient use of <i>VRML prototypes</i> withing the Java programming language and the JavaBeans standard. Thus, is substantiates the neccesarity of developing a new component model with runtime definition of compound data types. That is the topic of a research project which is held now by Software Engineering department of Higher School of Economics. </p><br>

## VRML Parser

<p>The VRML parser is built using the <i>recursive-descent parsing</i> technique, a top-down method which allows to develop the parser in a strict way according to the VRML grammar.</p>
<p>The parser performs the syntax analysis of the input VRML file: </p>

<pre><code>
DEF shape1 Shape
{
  appearance Appearance
  {
    material Material
    {
      diffuseColor 0 0.5 0
    }
  }
  geometry Sphere {}
}
USE shape1
</code></pre>

Using the knowledge about the VRML <i>Node types</i> and <i>data types</i>, it immediately builds a bunch of JavaBeans components, which make up the scene graph:</p>
<img src = "http://s019.radikal.ru/i616/1204/d9/5529bb600875.jpg"/>
<br><br>

## The front-end
<i>Under construction</i>
<br><br>


## Reference books:
<table border = "0" width = "60%">
<td valign = "bottom" width = "33%"><img src = "http://dragonbook.stanford.edu/cover.jpg" hspace = "5"/></td>
<td valign = "bottom" width = "33%"><img src = "http://www.horstmann.com/corejava/cj8v1.png" hspace = "5"/></td>
<td valign = "bottom" width = "33%"><img src = "http://www.horstmann.com/corejava/cj8v2.png" hspace = "5"/></td>
</table><br><br>

* __"Compilers: Principles, Techniques, and Tools"__ by Alfred V. Aho, Monica S. Lam, Ravi Sethi, and Jeffrey D. Ullman
* __"Core Java, Volume I: Fundamentals"__ by Cay S. Horstmann and Gary Cornell
* __"Core Java, Volume II: Advanced features"__ by Cay S. Horstmann and Gary Cornell