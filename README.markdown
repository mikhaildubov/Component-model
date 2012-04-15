<h1>Component model with declarative type definition</h1>

<p>The project is devoted to constructing a component model using its declarative description. The goal is to build a set of connected <em>JavaBeans</em> components, called the <em>scene graph</em>. The interactions between components are described with a declarative language, either <em>VRML</em> or <em>XML</em>. Then, a special <em>front-end</em> visualizes the scene graph and allows the user to change the properties of the components and the connections between them. </p>
<p>Besides, the project shows the impossibility of efficient use of <em>VRML prototypes</em> within the scope of the pure Java programming language and the JavaBeans standard. Thus, is substantiates the necessity of developing a new component model with runtime definition of compound data types. That is the topic of a research project which is held now by Software Engineering department of Higher School of Economics. </p><br/>

<h2>VRML Parser</h2>

<p>The VRML parser is built using the <em>recursive-descent parsing</em> technique, a top-down method which allows one to develop the parser in a strict way according to the VRML grammar.</p>
<p>The parser performs the syntax analysis of some input VRML file: </p>

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
<br/>

Using the knowledge about the VRML <em>Node types</em> and <em>data types</em>, it immediately builds a bunch of JavaBeans components, which make up the scene graph:</p>
<table border = "0">
<td>
<img src = "http://s019.radikal.ru/i616/1204/d9/5529bb600875.jpg"/>
</td>
</table>
<br/><br/>

<h2>The front-end</h2>
<em>Under construction</em>
<br/><br/>


## Reference books:
<table border = "0" width = "60%">
<td valign = "bottom" width = "33%"><img src = "http://s019.radikal.ru/i638/1204/e7/29b589960569.jpg" hspace = "5"/></td>
<td valign = "bottom" width = "33%"><img src = "http://www.horstmann.com/corejava/cj8v1.png" hspace = "5"/></td>
<td valign = "bottom" width = "33%"><img src = "http://www.horstmann.com/corejava/cj8v2.png" hspace = "5"/></td>
</table><br/>

* __"Compilers: Principles, Techniques, and Tools"__ by Alfred V. Aho, Monica S. Lam, Ravi Sethi, and Jeffrey D. Ullman
* __"Core Java, Volume I: Fundamentals"__ by Cay S. Horstmann and Gary Cornell
* __"Core Java, Volume II: Advanced features"__ by Cay S. Horstmann and Gary Cornell