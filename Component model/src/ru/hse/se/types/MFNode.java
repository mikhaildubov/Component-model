package ru.hse.se.types;

import java.util.ArrayList;

import ru.hse.se.nodes.Node;
import ru.hse.se.parsers.SyntaxError;
import ru.hse.se.parsers.VRMLParser;

public class MFNode extends MFType<Node> {

    public MFNode(ArrayList<Node> value) {
        super(value);
    }

    /**
     * Reads a MFNode value from the stream.
     * 
     ***************************************
     * mfnodeValue ::=                     *
     *         nodeStatement |             *
     *         [ ] |                       *
     *         [ nodeStatements ]          *
     ***************************************
     *
     * TODO: This method is definitely a "crutch". Any improvements?
     */
    public static MFNode parse(VRMLParser parser) throws SyntaxError {
        
        MFNode res = new MFNode(new ArrayList<Node>());
        
        try {
            if (parser.lookahead("[")) {
                parser.match("[");
                while(! parser.lookahead("]")) {
                    res.add(parser.parseChildNode());
                }
                parser.match("]");
            } else {
                res.add(parser.parseChildNode());
            }
            
        } catch (Exception e) {}
        
        return res;
    }

}
