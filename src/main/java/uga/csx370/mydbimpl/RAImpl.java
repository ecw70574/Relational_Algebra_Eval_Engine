package uga.csx370.mydbimpl;

import java.util.ArrayList;
import java.util.List;

import uga.csx370.mydb.Cell;
import uga.csx370.mydb.Predicate;
import uga.csx370.mydb.RA;
import uga.csx370.mydb.Relation;
import uga.csx370.mydb.RelationBuilder;
import uga.csx370.mydb.Type;


public class RAImpl implements RA {

    @Override
    public Relation select(Relation rel, Predicate p) {
        // TODO Auto-generated method stub

        Relation rel2 = new RelationBuilder()
                .attributeNames(rel.getAttrs())
                .attributeTypes(rel.getTypes())
                .build(); // Build new relation using supplied table

        int size = rel.getSize(); // number of rows in supplied relation 

        for (int i = 0; i < size; i++) { // Iterate through all the rows
            List<Cell> curr = rel.getRow(i); // Get row
            if (p.check(curr) == true) {  // Check the predicate: boolean method check
                rel2.insert(curr); // Inser into new relation
            } // if 
        } // for 

        return rel2; // Return the new relation
    }

    @Override
    public Relation project(Relation rel, List<String> attrs) {

        for (int i = 0; i < attrs.size(); i++) { // looping through the attributes that will be in new relation
            if (!rel.hasAttr(attrs.get(i))){  // check that the attribute is present in the original relation 
                throw new IllegalArgumentException("Attribute not present."); 
            } // if 
        } // for 

        List<Type> allTypes = rel.getTypes(); // all types from Relation
        List<Type> attrsType = new ArrayList<>(); // getting the types of the attrs param
        for (int i = 0; i < attrs.size(); i++) { // loop through each attribute we want
            String currAttr = attrs.get(i); // get column name of attribute
            int indexOG = rel.getAttrIndex(currAttr); // find its index in OG relation
            Type this_type = allTypes.get(indexOG); // get Type at that index in OG relation
            attrsType.add(this_type); // add to subset of Types in new relation
        }


        Relation rel2 = new RelationBuilder() // relation to be returned 
                .attributeNames(attrs) // only attributes passed through as param
                .attributeTypes(attrsType) // only the types corresponding to attribs in param
                .build();

        // List<String> relAttrs = rel.getAttrs();

        for (int i = 0; i < rel.getSize(); i++) { // for each row in relation 
            List<Cell> smallRow = new ArrayList<>(); 
            List<Cell> currRow = rel.getRow(i);
            for (int j = 0; j < attrs.size(); j++) {
                String currAttr = attrs.get(j);
                int indexOG = rel.getAttrIndex(currAttr);
                Cell thisCell = currRow.get(indexOG); // index from the og relation 
                smallRow.add(thisCell); // add to the subseted attributes 
            }
            rel2.insert(smallRow); // add the finished row to the relation 
        }

        return rel2; // returning relation with attrs 

        // TODO Auto-generated method stub

    }

    @Override
    public Relation union(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'union'");
    }

    @Override
    public Relation intersect(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'intersect'");
    }

    @Override
    public Relation diff(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'diff'");
    }

    @Override
    public Relation rename(Relation rel, List<String> origAttr, List<String> renamedAttr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rename'");
    }

    @Override
    public Relation cartesianProduct(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'cartesianProduct'");
    }

    @Override
    public Relation join(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

    @Override
    public Relation join(Relation rel1, Relation rel2, Predicate p) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
    }

}