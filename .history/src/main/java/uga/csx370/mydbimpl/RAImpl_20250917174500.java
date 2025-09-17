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

        // check that attributes are present in original relation
        for (int i = 0; i < attrs.size(); i++) { // looping through the attributes that will be in new relation
            if (!rel.hasAttr(attrs.get(i))){  // check that the attribute is present in the original relation 
                throw new IllegalArgumentException("Attribute not present."); 
            } // if 
        } // for 

        // attributeTypes for new relation
        List<Type> allTypes = rel.getTypes(); // all types from Relation
        List<Type> attrsType = new ArrayList<>(); // getting the types of the attrs param
        for (int i = 0; i < attrs.size(); i++) { // loop through each attribute we want
            String currAttr = attrs.get(i); // get column name of attribute
            int indexOG = rel.getAttrIndex(currAttr); // find its index in OG relation
            Type this_type = allTypes.get(indexOG); // get Type at that index in OG relation
            attrsType.add(this_type); // add to subset of Types in new relation
        }

        // build the new relation
        Relation rel2 = new RelationBuilder() // relation to be returned 
                .attributeNames(attrs) // only attributes passed through as param
                .attributeTypes(attrsType) // only the types corresponding to attribs in param
                .build();

        // List<String> relAttrs = rel.getAttrs();

        for (int i = 0; i < rel.getSize(); i++) { // for each row in relation 
            List<Cell> smallRow = new ArrayList<>(); // new row
            List<Cell> currRow = rel.getRow(i); // old row
            for (int j = 0; j < attrs.size(); j++) { // iterate through the row
                String currAttr = attrs.get(j); // Get ea cell
                int indexOG = rel.getAttrIndex(currAttr); // get index from og relation
                Cell thisCell = currRow.get(indexOG); // create new cell  
                smallRow.add(thisCell); // add cell to the subseted attributes 
            }
            rel2.insert(smallRow); // add the finished row to the relation 
        }

        return rel2; // returning relation with attrs 
    }

    @Override
    public Relation union(Relation rel1, Relation rel2) { 
        // TODO Auto-generated method stub

        // throw exception 

        // empyt relation 3 
        Relation rel3 = new RelationBuilder()
                .attributeNames(rel1.getAttrs())
                .attributeTypes(rel1.getTypes())
                .build(); // Build new relation using supplied table

        // copying relation 1 to make relation 3
        for (int i = 0; i < rel1.getSize(); i++) { // Iterate through all the rows
            List<Cell> curr = rel1.getRow(i); // Get row
                    rel3.insert(curr); // Inser into new relation
        } // for 

        // adding the rows of relation 2 to relation 1 if they are not duplicated
        for (int i = 0; i < rel2.getSize(); i ++) {
            int dup = 0; 
            List<Cell> currRow = rel2.getRow(i); // getting the row from 2nd relation
            for (int j = 0; j < rel3.getSize(); j++) { // comparing to all rows of relation 1 
                if (rowEquals(rel3.getRow(j), currRow)) { 
                    dup++; 
                } // if            
            } // checks all of relation 1 against current row of relation 2 

            if (dup == 0) { // if no duplicates 
                rel3.insert(currRow); // insert current row of relation 2 to relation 1
            } // if 
        } // for

        return rel3;

    }

    @Override
    public Relation intersect(Relation rel1, Relation rel2) {
        //1. check compatibility. Same # of cols, attribute names same order, types same order
        //check if number of cols same
        if (rel1.getAttrs().size() != rel2.getAttrs().size()) {
            throw new IllegalArgumentException("Relations are not compatible");
        }
        //attribute names and types same order?
        for (int i = 0; i < rel1.getAttrs().size(); i++) {
            String name1 = rel1.getAttrs().get(i);    //comparing attr1 vs attri2 col by col
            String name2 = rel2.getAttrs().get(i);
            Type type1 = rel1.getTypes().get(i);      //comparing type1 vs type2 col by col
            Type type2 = rel2.getTypes().get(i);

            if (!name1.equals(name2) || type1 != type2) {
                throw new IllegalArgumentException("Relations are not compatible.");
            }
        }

        //2. creating a empty table, same attributes/types as rel1
        Relation result = new RelationBuilder()
            .attributeNames(rel1.getAttrs())   
            .attributeTypes(rel1.getTypes())   
            .build();      
            
            // Step 3: Loop through every row in rel1
        for (int i = 0; i < rel1.getSize(); i++) {
            List<Cell> r1 = rel1.getRow(i);   // current row from rel1

            // Step 4: See if this row exists in rel2
            boolean existsInSecond = false;
            for (int j = 0; j < rel2.getSize(); j++) {
                List<Cell> r2 = rel2.getRow(j);
                if (rowEquals(r1, r2)) {
                    existsInSecond = true;
                    break; // stop searching once we find a match
                }
            }

            // If not found in rel2, skip this row
            if (!existsInSecond) {
                continue;
            }

            // Step 5: Check if this row is already in the result
            boolean alreadyAdded = false;
            for (int k = 0; k < result.getSize(); k++) {
                List<Cell> rowInResult = result.getRow(k);
                if (rowEquals(r1, rowInResult)) {
                    alreadyAdded = true;
                    break;
                }
            }

            // Step 6: If not already present, insert it
            if (!alreadyAdded) {
                result.insert(new ArrayList<>(r1)); // shallow copy of the row
            }
        }

    // Step 7: Return the result
    return result;
}




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

    // Helper Methods

    /*
     * Checks row equality
     */
    public boolean rowEquals(List<Cell> row1, List<Cell> row2) {
        if (row1.size() != row2.size()) { // if row sizes equal
            return false;
        } //if
        for(int i = 0; i < row1.size(); i++) { //iterate through the rows
            if(row1.get(i).getAsString().equals(row2.get(i).getAsString()) == false) {
                return false; // values not same
            }
        }
        return true; //equal
    } //rowEquals
}