package ru.hse.se.parsers;

import ru.hse.se.nodes.Node;

import java.io.*;
import java.util.ArrayList;

/**
 * An abstract parser class that defines
 * the basic items each parser should
 * have to analyze a scene.
 * 
 * @author Mikhail Dubov
 */
public abstract class Parser {

    public ArrayList<Node> parse(InputStreamReader reader) {

        tokenizer = new StreamTokenizer(new BufferedReader(reader));
        
        // TODO: Encoding issue??? *.wrl UTF8 -> bad; ANSI -> ok
        
        setUpTokenizer();
        ArrayList<Node> sceneGraph = parseScene();

        return sceneGraph;
    }

    protected abstract void setUpTokenizer();
    protected abstract ArrayList<Node> parseScene();

    protected StreamTokenizer tokenizer;
}
