<h1>Component model with declarative type definition</h1>

<p>The project is targeted at constructing a component model using its declarative description. The goal is to build a set of connected <a href="http://www.oracle.com/technetwork/java/javase/tech/index-jsp-138795.html">JavaBeans</a> components, called the <em>scene graph</em>. The interactions between components are described in a declarative language, either <a href="http://www.web3d.org/x3d/specifications/vrml/">VRML</a> or <a href="http://en.wikipedia.org/wiki/XML">XML</a>. Then, a special <em>front-end</em> visualizes the scene graph and allows the user to change the properties of the components as well as the connections between them. </p>
<p>Besides, the project demonstrates the impossibility of efficient use of <a href="http://www.web3d.org/x3d/specifications/vrml/ISO-IEC-14772-VRML97/part1/concepts.html#4.8">VRML prototypes</a> within the scope of the pure Java programming language and the JavaBeans standard. Thus, is substantiates the necessity of developing a new component model with runtime definition of compound data types. That is the topic of a research project which is held now by <a href="http://se.hse.ru/">Software Engineering department</a> of Higher School of Economics. </p><br/>

<h2>VRML Parser</h2>

<p>The VRML parser is built using the <a href="http://en.wikipedia.org/wiki/Recursive_descent_parser">recursive-descent parsing</a> technique, a top-down method which allows one to develop the parser in a strict way according to the <a href="http://www.web3d.org/x3d/specifications/vrml/ISO-IEC-14772-VRML97/part1/grammar.html">VRML grammar</a>.</p>
<p>The parser performs the syntax analysis of some input VRML file: </p>

<pre><code>DEF shape1 Shape
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

USE shape1</code></pre>

<p>&ndash; and, using its knowledge about the VRML <em>node types</em> and <em>data types</em>, immediately builds a bunch of <em>JavaBeans</em> components, which make up the scene graph:</p>
<table border = "0">
<td>
<img src = "http://s019.radikal.ru/i616/1204/d9/5529bb600875.jpg"/>
</td>
</table>
<br/><br/>

<h2>XML Parser</h2>
<p>The XML parser is built according to <a href="http://en.wikipedia.org/wiki/Simple_API_for_XML">SAX</a>, "Simple API for XML", which is an event-based model of parser construction. Here, the parser operates on each piece of the XML document sequentially, reporting special events as soon as he meets an opening tag, an attribute etc.:</p>
<pre><code>openingTag(name)
closingTag(name)
attribute(name, value)
textNode(text)</code></pre>
<p>One of the benefits of this approach is an effective use of memory, as there is no need of tree representation of the entire document. </p>
<br/><br/>

<h2>Front-end</h2>
<em>Under construction</em>
<br/><br/>


<h2>Reference books:</h2>
<table border = "0" width = "60%">
<td valign = "bottom" width = "33%"><img src = "http://s019.radikal.ru/i638/1204/e7/29b589960569.jpg" hspace = "5"/></td>
<td valign = "bottom" width = "33%"><img src = "http://www.horstmann.com/corejava/cj8v1.png" hspace = "5"/></td>
<td valign = "bottom" width = "33%"><img src = "http://www.horstmann.com/corejava/cj8v2.png" hspace = "5"/></td>
</table>

* __"Compilers: Principles, Techniques, and Tools"__ by Alfred V. Aho, Monica S. Lam, Ravi Sethi, and Jeffrey D. Ullman
* __"Core Java, Volume I: Fundamentals"__ by Cay S. Horstmann and Gary Cornell
* __"Core Java, Volume II: Advanced features"__ by Cay S. Horstmann and Gary Cornell
<br/><br/>

<h2>Collaborators</h2>
<b><a href="http://github.com/msdubov">Mikhail Dubov</a></b> <em>(parsers)</em><br/>
<b><a href="http://github.com/istima">Timur Akhmetgareev</a></b> <em>(front-end)</em><br/>
<br/>
<b>Efim Grinkrug</b> <em>(supervisor)</em>