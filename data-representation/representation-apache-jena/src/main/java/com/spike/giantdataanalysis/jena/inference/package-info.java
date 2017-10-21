/**
 * Reasoner和Rule Engine.
 * 
 * <pre>
 * Available reasoners¶Included in the Jena distribution are a number of predefined reasoners:
 * 
 * 1. Transitive reasoner: Provides support for storing and traversing class and property lattices.    This implements just the transitive and reflexive properties    of rdfs:subPropertyOf and rdfs:subClassOf.
 * 2. RDFS rule reasoner: Implements a configurable subset of the RDFS entailments.
 * 3. OWL, OWL Mini, OWL Micro Reasoners:   A set of useful but incomplete implementation of the OWL/Lite subset of the OWL/Full    language. 
 * 4. Generic rule reasoner: A rule based reasoner that supports user defined rules. Forward chaining,    tabled backward chaining and hybrid execution strategies are supported.
 * </pre>
 * <p>
 * REF: https://jena.apache.org/documentation/inference/
 * @author zhoujiagen
 */
package com.spike.giantdataanalysis.jena.inference;