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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'intersect'");
    }

    @Override
    public Relation diff(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub

        // Column size must be the same
        List<String> attrs1 = rel1.getAttrs();
        List<String> attrs2 = rel1.getAttrs();
        if(attrs1.size() != attrs2.size()) { // diff num of attributes
            throw new IllegalArgumentException("Attributes of Relations Differ.");
        } //if
        // Column names must be the same
        for (int i = 0; i < attrs1.size(); i++) {
            if (attrs1.get(i).equals(attrs2.get(i))) { // diff attribute names
                throw new IllegalArgumentException("Attributes of Relations Differ.");
            } //if
        } //for
        // Columns types must be the same
        List<Type> attrtypes1 = rel1.getTypes(); // list of attr types from rel1
        List<Type> attrtypes2 = rel1.getTypes(); // list of attr types from rel2
        for (int i = 0; i < attrtypes1.size(); i++) { // diff attribute types
            if (attrtypes1.get(i) != attrtypes2.get(i)) {
                throw new IllegalArgumentException("Attributes of Relations Differ.");
            } //if
        } //for

        throw new UnsupportedOperationException("Unimplemented method 'diff'");
    }

    @Override
    public Relation rename(Relation rel, List<String> origAttr, List<String> renamedAttr) {
        // TODO Auto-generated method stub
        //List of attribute name row
        List<Cell> attrNames = rel.getRow(0);

        // rename attributes 
        for (int i = 0; i < origAttr.size(); i++)  {
            //make sure table actually has attribute
            //make sure origAttr and rename Attr are same size
            String oldName = origAttr.get(i);
            String newName = renamedAttr.get(i);
            int index = rel.getAttrIndex(oldName);
            attrNames.set(index, Cell.val(newName));
        } // for
        
        //turns List<Cell> into List<String>
        List<String> attrNamesString = new ArrayList<>();
        for (int i = 0; i < attrNames.size(); i++) {
            attrNamesString.add(attrNames.get(i).getAsString());
        }
        
        Relation rel2 = new RelationBuilder()
                .attributeNames(attrNamesString)
                .attributeTypes(rel.getTypes())
                .build();

        
        //insert new attributes row
        rel2.insert(attrNames);

        //insert the remaining rows
        for (int i = 1; i < rel.getSize(); i++) {
            rel2.insert(rel.getRow(i));
        } // for
        
        return rel2;
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