
import static org.junit.jupiter.api.Assertions.*; // org.junit.Assert.*; 
import org.junit.After;
import org.junit.Before;
import org.junit.Test;



/** 
 * The test class for HashTable
 * tests the various methods from HashTable and ensures they are working properly
 */
public class HashTableTest{
	
	HashTable hash;
    
    @Before
    public void setUp() throws Exception {
    	hash = new HashTable(11, 0.75); // Creates a new HashTable of size 11 and a Load Factor Threshold of 0.75
    }

    @After
    public void tearDown() throws Exception {
    	hash = null; // Empty the HashTable
    }
    
    /** 
     * Tests that a HashTable returns an integer code
     * indicating which collision resolution strategy 
     * is used.
     * REFER TO HashTableADT for valid collision scheme codes.
     */
    @Test
    public void test000_collision_scheme() {
        HashTableADT htIntegerKey = new HashTable<Integer,String>();
        int scheme = htIntegerKey.getCollisionResolution();
        if (scheme < 1 || scheme > 9) 
            fail("collision resolution must be indicated with 1-9");
    }
        
    /** IMPLEMENTED AS EXAMPLE FOR YOU
     * Tests that insert(null,null) throws IllegalNullKeyException
     */
    @Test
    public void test001_IllegalNullKey() {
        try {
            HashTableADT htIntegerKey = new HashTable<Integer,String>();
            htIntegerKey.insert(null, null);
            fail("should not be able to insert null key");
        } 
        catch (IllegalNullKeyException e) { /* expected */ } 
        catch (Exception e) {
            fail("insert null key should not throw exception "+e.getClass().getName());
        }
    }
    
    /**
     * Tests the insert and get method, makes sure the value from get() is correct
     */
	@Test
    public void test002_insert_2_check_get() {
    	try {
			hash.insert("key", 10);
			if (!hash.get("key").equals(10))
				fail("get method failed to return the correct value");
		} catch (IllegalNullKeyException | DuplicateKeyException | KeyNotFoundException e) {
			fail("should not throw an exception");
		}
    }
	
    /**
     * Tests the insert method and makes sure that numKeys properly counts the amount of keys
     * in the HashTable
     */
    @Test
    public void test003_insert_4_check_numKeys() {
    	try {
			hash.insert("key1", 1);
			hash.insert("key2", 2);
			hash.insert("key3", 3);
			hash.insert("key4", 4);
			if (hash.numKeys() != 4)
				fail("numKeys() did not return the correct number of keys");
		} catch (IllegalNullKeyException | DuplicateKeyException e) {
			fail("should not throw an exception");
		}
    }
    
    /**
     * Inserts then removes a key, checks that numKeys is correct
     */
    @Test
    public void test004_insert_remove_check_numKeys() {
    	try {
			hash.insert("key1", "one");
			if (hash.numKeys() != 1)
				fail("insert did not correctly update numKeys");
			hash.remove("key1");
			if (hash.numKeys() != 0)
				fail("insert then remove did not correctly update numKeys");
		} catch (IllegalNullKeyException | DuplicateKeyException e) {
			fail("should not throw an exception");
		}
    }
    
    /**
     * Inserts two keys into the HashTable, then checks that the Load Factor
     * is calculated correctly
     */
    @Test
    public void test005_insert_2_check_loadfactor() {
    	try {
			hash.insert("key1", 1);
			hash.insert("key2", "two");
			if (hash.getLoadFactor() != (1.0 * 2) / hash.getCapacity())
				fail("getLoadFactor() didn't return the correct value " + hash.getLoadFactor());
		} catch (IllegalNullKeyException | DuplicateKeyException e) {
			fail("should not throw an exception");
		}
    }
    
    /**
     * Checks that the initialCapacity and the LoadFactorThreshold are set and assigned properly
     */
    @Test
    public void test006_check_loadfactor_and_initial_capacity() {
    	if(hash.getLoadFactor() != 0.75 && hash.getCapacity() != 11)
    		fail("Initial load factor and capacity were incorect");
    }
    
    /**
     * After nine keys have been inserted the HashTable should ReHash
     * then check that numKeys is correct
     */
    @Test
    public void test007_insert_9_table_rehash_check_numKeys() {
    	try {
			hash.insert("key1", 1);
			hash.insert("key2", 2);
			hash.insert("key3", 3);
			hash.insert("key4", 4);
			hash.insert("key5", 5);
			hash.insert("key6", 6);
			hash.insert("key7", 7);
			hash.insert("key8", 8);
			hash.insert("key9", 9);
		} catch (IllegalNullKeyException | DuplicateKeyException e) {
			fail("should not throw an exception");
		}
    	if (hash.numKeys() != 9)
    		fail("numKeys is not updating its value properly, should be 9 but is " + hash.numKeys());
    }
    
    /**
     * Checks that after 9 keys are inserted that the table has rehashed and the capacity is correct to
     * the formula capacity = capacity * 2 + 1
     */
    @Test
    public void test008_insert_9_table_rehash_check_capacity() {
    	try {
			hash.insert("key1", 1);
			hash.insert("key2", 2);
			hash.insert("key3", 3);
			hash.insert("key4", 4);
			hash.insert("key5", 5);
			hash.insert("key6", 6);
			hash.insert("key7", 7);
			hash.insert("key8", 8);
			hash.insert("key9", 9);
			if (hash.getCapacity() != 23) {
				fail("Size was not correct " + hash.getCapacity());
			}
		} catch (IllegalNullKeyException | DuplicateKeyException e) {
			fail("should not throw an exception");
		}
    }
    
    @Test
    public void test009_remove_key_not_in_table() {
    	try {
			hash.insert("key1", "one");
			if (hash.remove("key2") != false)
				fail("remove should have returned false, key is not in the table");
		} catch (IllegalNullKeyException | DuplicateKeyException e) {
			fail("should not throw exception");
		}
    }
}
