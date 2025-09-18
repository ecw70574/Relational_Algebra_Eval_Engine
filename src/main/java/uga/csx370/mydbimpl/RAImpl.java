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

        // framework for new relation w
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
            } // for 
            rel2.insert(smallRow); // add the finished row to the relation 
        } // for 
        return rel2; // returning relation with attrs 
    } // project

    @Override
    public Relation union(Relation rel1, Relation rel2) { 

        //1. check compatibility. Same # of cols, attribute names same order, types same order
        //check if number of cols same

        if (rel1.getAttrs().size() != rel2.getAttrs().size()) {
            throw new IllegalArgumentException("Relations are not compatible");
        } // if 

        //attribute names and types same order?
        for (int i = 0; i < rel1.getAttrs().size(); i++) {
            String name1 = rel1.getAttrs().get(i);    //comparing attr1 vs attri2 col by col
            String name2 = rel2.getAttrs().get(i);
            Type type1 = rel1.getTypes().get(i);      //comparing type1 vs type2 col by col
            Type type2 = rel2.getTypes().get(i);

            if (!name1.equals(name2) || type1 != type2) {
                throw new IllegalArgumentException("Relations are not compatible.");
            } // if
        } // for

        // framework for relation 3
        Relation rel3 = new RelationBuilder()
                .attributeNames(rel1.getAttrs())
                .attributeTypes(rel1.getTypes())
                .build(); // Build new relation using supplied table

        // copying relation 1 to make relation 3
        for (int i = 0; i < rel1.getSize(); i++) { // Iterate through all the rows
            List<Cell> curr = rel1.getRow(i); // Get row
                    rel3.insert(curr); // Insert into new relation
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

            
        //3. going through every row in rel 1 - large outer loop
        for (int i = 0; i < rel1.getSize(); i++) {
            List<Cell> r1 = rel1.getRow(i);   

            //4. check if this specific row exists in rel2
            boolean existsInSecond = false;
            for (int j = 0; j < rel2.getSize(); j++) {
                List<Cell> r2 = rel2.getRow(j);
                if (rowEquals(r1, r2)) {
                    existsInSecond = true;
                    break; // stop - once we find a match
                }
            }

            //if this specific row not same in both, don't implement code below
            //no do step 5, go to the next iteration in outer loop - next row
            if (!existsInSecond) {
                continue;
            }

            //5. check for duplicates - is this row already in result table?
            boolean alreadyAdded = false;
            for (int k = 0; k < result.getSize(); k++) {
                List<Cell> rowInResult = result.getRow(k);
                if (rowEquals(r1, rowInResult)) {
                    alreadyAdded = true;
                    break;
                }
            }

            //6. if not already present, insert row into result table
            if (!alreadyAdded) {
                result.insert(new ArrayList<>(r1)); // shallow copy of the row
            }
        }
        //7: Return the result
    return result;
}

    @Override
    public Relation diff(Relation rel1, Relation rel2) {
        
        // Column size must be the same
        List<String> attrs1 = rel1.getAttrs();
        List<String> attrs2 = rel2.getAttrs();
        if(attrs1.size() != attrs2.size()) { // diff num of attributes
            throw new IllegalArgumentException("Attributes of Relations Differ.");
        } //if
        // Column names must be the same
        for (int i = 0; i < attrs1.size(); i++) {
            if (attrs1.get(i).equals(attrs2.get(i)) == false) { // diff attribute names
                throw new IllegalArgumentException("Attributes of Relations Differ.");
            } //if
        } //for
        // Columns types must be the same
        List<Type> attrtypes1 = rel1.getTypes(); // list of attr types from rel1
        List<Type> attrtypes2 = rel2.getTypes(); // list of attr types from rel2
        for (int i = 0; i < attrtypes1.size(); i++) { // diff attribute types
            if (attrtypes1.get(i) != attrtypes2.get(i)) {
                throw new IllegalArgumentException("Attributes of Relations Differ.");
            } //if
        } //for

        // create empty table w/ same attributes/types as rel1
        Relation result = new RelationBuilder()
            .attributeNames(rel1.getAttrs())   
            .attributeTypes(rel1.getTypes())   
            .build();  

        // go through rows in rel1
        for (int i = 0; i < rel1.getSize(); i++) {
            List<Cell> r1 = rel1.getRow(i); // rel1 row
            // check if row exists in rel2
            boolean existsInRel2 = false;
            for (int j = 0; j < rel2.getSize(); j++) { //go through rows in rel2
                List<Cell> r2 = rel2.getRow(j); // rel2 row
                if (rowEquals(r1, r2)) { //the rows are equal: do not add into resulting table
                    existsInRel2 = true;
                    break; //break out of for loop
                } //if
            } //for
            //insert
            if(existsInRel2 == false) { // if in rel1 but not in rel2
                result.insert(new ArrayList<>(r1)); //insert into result table
            } //if
        } //for

        return result; //return new table
    }

    @Override
    public Relation rename(Relation rel, List<String> origAttr, List<String> renamedAttr) {
        // TODO Auto-generated method stub
        
        if (origAttr.size() != renamedAttr.size()) {
            throw new IllegalArgumentException("the origAttr and renamedAttr must be the same lenght");
        }

        List<String> attrNames = new ArrayList<>(rel.getAttrs()); //list of attributes 

        // rename attributes 
        for (int i = 0; i < origAttr.size(); i++)  {
            String oldName = origAttr.get(i);
            String newName = renamedAttr.get(i);
            int index = rel.getAttrIndex(oldName);
            attrNames.set(index, newName);


        } // for
                
        Relation rel2 = new RelationBuilder()
                .attributeNames(attrNames)
                .attributeTypes(rel.getTypes())
                .build();

        //insert the remaining rows
        for (int i = 1; i < rel.getSize(); i++) {
            rel2.insert(rel.getRow(i));
        } // for
        return rel2;
    }

    @Override
    public Relation cartesianProduct(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        //throw new UnsupportedOperationException("Unimplemented method 'cartesianProduct'");
        // Output Relation will have all columns from first & all columns from second.
        // For each row combination in 1st Relation, will have a row in new relation horizontally appended 
        // with each row combo from 2nd Relation. No predicate needed, normally very expensive.

        ///First implementing case where rel1 and rel2 have no common attribute names                                                 
        List<String> r1attrs = rel1.getAttrs(); // get attributes of first relation                                                  
        List<Type> r1types = rel1.getTypes(); // get types of first relation                                                         
        List<String> r2attrs = rel2.getAttrs(); // get attributes of 2nd relation                                                     
        List<Type> r2types = rel2.getTypes(); // get types of 2nd relation   

        // Check if column names in common    
        for (int i = 0; i < rlattrs.size(); i++){ // iterate through all attributes from rel1
            for (int j = 0; j < r2attrs.size(); j++){ // iterate through all attributes from rel2
                if (r1attrs.get(i) == r2attrs.get(j)){
                    /*
                    String rel1name = "rel1." + r1attrs.get(i);
                    String rel2name = "rel2." + r2attrs.get(j);
                    r1attrs.set(i, rel1name);
                    r2attrs.set(i, rel2name)
                    */ 
                   throw new IllegalArgumentException("Relation 1 and Relation 2 have common attribute names");
                }
            }
        }                                                    
        List<String> allAttrs = r1attrs;
        List<Type> allTypes = r1types;
        allAttrs.addAll(r2attrs); // combine attributes into one list                                                                 
        allTypes.addAll(r2types); // combine types into one list                                                                      

        Relation rel3  = new RelationBuilder()
                .attributeNames(allAttrs)
                .attributeTypes(allTypes)
                .build();
        //        List<Cell> combined_row = new ArrayList<>(); // holds row that contains all rel1 cell elements and rel2 cell elements                                                                                                                          
        for(int rel1index = 0; rel1index < rel1.getSize(); rel1index++) {
            for(int rel2index = 0; rel2index < rel2.getSize(); rel2index ++){
                //combined_row.clear();                                                                                               
                List<Cell> rel1_row = rel1.getRow(rel1index);
                List<Cell> rel2_row = rel2.getRow(rel2index);
                rel1_row.addAll(rel2_row); // rel 1 elements are on left of list and rel2 elements appended to the right              
                rel3.insert(rel1_row); // insert into empty relation that has column names from each relation                         
            }
        }
        return rel3;

    }

    @Override
    public Relation join(Relation rel1, Relation rel2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
        // Natural join, joining columns with the same name and type from each Relation.
        // Can call working cartesian product function first
    }

    @Override
    public Relation join(Relation rel1, Relation rel2, Predicate p) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'join'");
        // Theta join, can explicitly define predicate. 
        // Can call working cartesian product function first. 
    }

    // Helper Methods

    /*
     * Checks row equality
     */
    public boolean rowEquals(List<Cell> row1, List<Cell> row2) {
        if (row1.size() != row2.size()) { // if row sizes equal
            return false;
        } //if

        for (int i = 0; i < row1.size(); i++) {
            if (row1.get(i).equals(row2.get(i)) == false) { //compare via .equals
            return false;
        }
    }
        return true; //equal
    } //rowEquals

}