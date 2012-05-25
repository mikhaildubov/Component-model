<h1>Component model with declarative type definition</h1>

<p>The project is targeted at constructing a component model using its declarative description. The goal is to build a set of connected <a href="http://www.oracle.com/technetwork/java/javase/tech/index-jsp-138795.html">JavaBeans</a> components, called the <em>scene graph</em>. The interactions between components are described in a declarative language, either <a href="http://www.web3d.org/x3d/specifications/vrml/">VRML</a> or <a href="http://en.wikipedia.org/wiki/XML">XML</a>. Then, a special <em>front-end</em> visualizes the scene graph and allows the user to change the properties of the components as well as the connections between them. </p>
<p>The main project features are:</p>
<ul>
<li> 2 parsers for VRML and X3D built using 2 different algorithms;
<li> Probably the best error diagnostic tools incorporated into the parsers;
<li> Codegenerators, which enable conversion between VRML and X3D formats;
<li> A front-end that visualizes the declarative description of component models.
</ul>
<br/>

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
<p>One of the benefits of this approach is a relatively effective use of memory, as there is no need of tree representation of the entire document. </p>
<br/><br/>

<h2>Front-end</h2>
<p>The front-end allows one to visualize component models using the scene graph produced by one of the parsers.</p>
<img src = "http://s019.radikal.ru/i605/1205/b6/97a67257d88c.png" hspace = "5"/>
<br/><br/>

<h2>Documentation</h2>
<p>The project comes along with the appropriate <a href="http://en.wikipedia.org/wiki/GOST">GOST</a> Documentation.</p>
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