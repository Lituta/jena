/*
 * (c) Copyright 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.suites;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.hp.hpl.jena.util.iterator.SingletonIterator;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.shared.PrefixMapping;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;

import com.hp.hpl.jena.sparql.path.Path;
import com.hp.hpl.jena.sparql.path.PathEval;
import com.hp.hpl.jena.sparql.path.PathParser;
import com.hp.hpl.jena.sparql.util.Utils;
import com.hp.hpl.jena.sparql.util.graph.GraphUtils;

public class TestPath extends TestCase
{
    public static TestSuite suite()
    {
        TestSuite ts = new TestSuite(TestPath.class) ;
        ts.setName(Utils.classShortName(TestPath.class)) ;
        return ts ;
    }

    static Graph graph1 = GraphUtils.makeDefaultGraph() ;
    static Graph graph2 = GraphUtils.makeDefaultGraph() ;
    
    static Node n1 = Node.createURI("n1") ;
    static Node n2 = Node.createURI("n2") ;
    static Node n3 = Node.createURI("n3") ;
    static Node n4 = Node.createURI("n4") ;
    static Node p = Node.createURI("http://example/p") ;
    static PrefixMapping pmap  = new PrefixMappingImpl() ;
    
    static {
        pmap.setNsPrefixes(PrefixMapping.Standard) ;
        pmap.setNsPrefix("", "http://example/") ;
        
        graph1.add(new Triple(n1, p, n2)) ;
        graph1.add(new Triple(n2, p, n3)) ;
        graph1.add(new Triple(n3, p, n4)) ;
    }
    
    // ----
    
    public void testPath_01()           { test(n1, ":p", new Node[]{n2}) ; }

    public void testPath_02()           { test(n1, ":p{0}", new Node[]{n1}) ; }

    public void testPath_03()           { test(n1, ":p{1}", new Node[]{n2}) ; }

    public void testPath_04()           { test(n1, ":p{2}", new Node[]{n3}) ; }

    public void testPath_05()           { test(n1, ":p{0,1}", new Node[]{n1, n2}) ; }

    public void testPath_06()           { test(n1, ":p{0,2}", new Node[]{n1,n2,n3}) ; }

    public void testPath_07()           { test(n1, ":p{1,2}", new Node[]{n2, n3}) ; }

    public void testPath_08()           { test(n1, ":p{9,9}", new Node[]{}) ; }

    public void testPath_09()           { test(n1, ":p*", new Node[]{n1,n2,n3,n4}) ; }

    public void testPath_10()           { test(n1, ":p+", new Node[]{n2,n3,n4}) ; }
    
    public void testPath_11()           { test(n1, ":p?", new Node[]{n1,n2}) ; }

    // ----
    private void test(Node start, String string, Node[] expectedNodes)
    {
        Path p = PathParser.parse(string, pmap) ;
        Iterator resultsIter = PathEval.eval(graph1, new SingletonIterator(start), p) ;
        Set results = new HashSet() ;
        for ( ; resultsIter.hasNext() ; )
            results.add( (Node)resultsIter.next() ) ;

        Set expected = new HashSet(Arrays.asList(expectedNodes)) ;
        assertEquals(expected, results) ;
    }
}

/*
 * (c) Copyright 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */