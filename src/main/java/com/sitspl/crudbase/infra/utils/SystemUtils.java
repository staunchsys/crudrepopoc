package com.sitspl.crudbase.infra.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class SystemUtils {

    private static SystemUtils me;

    public static SystemUtils getInstance() {
        if (me == null)
            me = new SystemUtils();

        return me;
    }

    public String resolveContentType(String filename) {
        String ret = "";

        if (filename.endsWith(".pdf"))
            ret = "application/pdf";
        if (filename.endsWith(".xls"))
            ret = "application/vnd.ms-excel";
        if (filename.endsWith(".doc"))
            ret = "application/msword";
        if (filename.endsWith(".zip"))
            ret = "application/x-zip";

        return ret;
    }

    /*
     * public <K, V> Map<K, V> buildMap(Map<K, V> map, Object... data) { if (data !=
     * null) { if (data.length % 2 != 0) { throw new
     * RuntimeException("Data must be in even key-value pairs."); }
     * 
     * for (int i = 0; i < data.length; i += 2) { K key = (K) data[i]; V value = (V)
     * data[i + 1];
     * 
     * map.put(key, value); } }
     * 
     * return map; }
     */

    public <K, V> Map<K, V> buildMap(Map<K, V> map, Object... data) {
        if (data != null) {
            for (int i = 0; i < data.length; i += 2) {
                K key = (K) data[i];
                if (key instanceof List) { // expand this array
                    List<Object[]> arr = (List<Object[]>) key;

                    for (int j = 0; j < arr.size(); j++) {
                        Object[] arrrow = arr.get(j);
                        if (arrrow.length != 2) {
                            throw new RuntimeException("Array values must be in pairs");
                        }
                        map.put((K) arrrow[0], (V) arrrow[1]);
                    }

                    i--; // minus one so the i+=2 remains accurate
                } else {
                    V value = (V) data[i + 1];
                    map.put(key, value);
                }
            }
        }

        return map;
    }
    
    public Map<String, Object> buildDataMap(Object... data) {
        Map<String, Object> dataMap = buildMap(new HashMap<String, Object>(), data);
        return buildMap(new HashMap<String, Object>(), "data", dataMap);
    }

    // public Map<String, Object> buildDataMap(Map<String, Object> map, Object... data) {
    // if (data != null) {
    // if (data.length % 2 != 0) {
    // throw new RuntimeException("Data must be in even key-value pairs.");
    // }
    //
    // for (int i = 0; i < data.length; i += 2) {
    // K key = (K) data[i];
    // V value = (V) data[i + 1];
    //
    // map.put(key, value);
    // }
    // }
    // Map dataMap = new LinkedHashMap<>();
    // map.put("data", map);
    // return map;
    // }

    public Set sortSet(Set set, Comparator comparator) {
        List bufferList = new ArrayList();
        bufferList.addAll(set);
        Collections.sort(bufferList, comparator);
        set = null;
        set = new LinkedHashSet();
        for (Iterator itr = bufferList.iterator(); itr.hasNext();)
            set.add(itr.next());
        bufferList = null;

        return set;
    }

    public String getSimpleClassName(String className) {
        return StringUtils.getInstance().getLastToken(className, ".");
    }

    public Integer getIndexInMap(Map map, Object key) {
        Integer count = 0;

        for (Iterator itr = map.keySet().iterator(); itr.hasNext(); count++) {
            if (key.equals(itr.next()))
                break;
        }
        if (count == map.size())
            count = -1;

        return count;
    }

    // ************************************** ARRAY METHODS ****************************************
    public boolean findStringInStringArray(String string, String[] array) {
        boolean ret = false;

        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(string)) {
                ret = true;
                break;
            }
        }

        return ret;
    }

    public String[] buildStringArrayFromString(String string, String delim) {
        if (string != null) {
            StringTokenizer st = new StringTokenizer(string, delim);
            String[] ret = new String[st.countTokens()];

            int count = 0;
            while (st.hasMoreTokens()) {
                ret[count] = st.nextToken();
                count++;
            }

            return ret;
        } else
            return null;
    }

    public String buildStringFromStringArray(String[] array, String delim) {
        StringBuffer ret = new StringBuffer();

        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                ret.append(array[i]);

                if (i + 1 < array.length)
                    ret.append(delim);
            }
        }

        return ret.toString();
    }

    public List<String> buildListFromString(String string, String delim) {
        return buildListFromString(string, delim, false);
    }

    public List<String> buildListFromString(String string, String delim, boolean includeEmpty) {
        if (string == null)
            return null;
        List<String> ret = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(string, delim, includeEmpty);
        while (st.hasMoreTokens()) {
            String token = st.nextToken().trim();
            if (includeEmpty && token.equals(delim)) { // empty string
                ret.add(null);
                if (!st.hasMoreTokens())
                    ret.add(null); // if is last token, add one more
            } else {
                ret.add(token);
                if (includeEmpty && st.hasMoreTokens()) {
                    st.nextToken(); // take out delim
                    if (!st.hasMoreTokens())
                        ret.add(null); // if is last token, add one more
                }
            }
        }

        return ret;
    }

    public String buildStringFromList(List list, String delim) {
        String[] arr = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            arr[i] = list.get(i).toString();
        return buildStringFromStringArray(arr, delim);
    }

    // *********************************** OBJECT REFLECTION METHODS ***********************************
    public void populate(Object dest, Map orig) {
        if (dest != null && orig != null) {
            for (Iterator itr = orig.keySet().iterator(); itr.hasNext();) {
                Object key = itr.next();
                Object value = orig.get(key);

                try {
                    if (value != null && value.toString().length() > 0 && !value.toString().equals("0")) {
                        String setterMethodName = buildFieldSetter(key.toString());
                        Method setterMethod = dest.getClass().getMethod(setterMethodName, new Class[] { value.getClass() });

                        try {
                            setterMethod.invoke(dest, new Object[] { value });
                        } catch (InvocationTargetException e) {
                            log.error(e.getMessage(), e);
                        } catch (IllegalAccessException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                } catch (NoSuchMethodException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    /*
    	public void copyProperties(Object dest, Object orig) {
    		copyProperties(dest, orig, false);
    	}
    	
    	public void copyPropertiesDeep(Object dest, Object orig) {
    		copyProperties(dest, orig, true);
    	}
    	
    	private void copyProperties(Object dest, Object orig, boolean deep) {
    		if (dest != null && orig != null) {
    			Field[] fields = dest.getClass().getDeclaredFields();
    
    			for (int i = 0; i < fields.length; i++) {
    				Field field = fields[i];
    
    				try {
    					String setterMethodName = buildFieldSetter(field.getName());
    					Method setterMethod = dest.getClass().getMethod(setterMethodName, new Class[] { field.getType() });
    
    					Object origValue = getObjectValue(orig, field.getName());
    					if (origValue != null) {
    						try {
    							// if is deep copy, then recurse in. otherwise just invoke.
    							if (deep && origValue.getClass().getName().contains(AUDIT_FIELD_MODEL_PACKAGE_NAME)) {
    								// get the same composite object from dest
    								Object destValue = getObjectValue(dest, field.getName());
    								// if dest is null, instantiate
    								if (destValue == null) {
    									try {
    										destValue = field.getType().newInstance();
    									}
    									catch (InstantiationException e) {
    										log.error(e.getMessage(), e);
    									}
    								}
    								// recurse in, copy value of entity
    								copyProperties(destValue, origValue, deep);
    							}
    							setterMethod.invoke(dest, new Object[] { origValue });
    						}
    						catch (InvocationTargetException e) {
    							log.error(e.getMessage(), e);
    						}
    						catch (IllegalAccessException e) {
    							log.error(e.getMessage(), e);
    						}
    					}
    				}
    				catch (NoSuchMethodException e) {
    					// do nothing
    				}
    			}
    		}
    	}
    */
    public String buildFieldGetter(String fieldName) {
        char[] fieldNameChars = fieldName.toCharArray();
        fieldNameChars[0] = Character.toUpperCase(fieldNameChars[0]);

        return "get" + new String(fieldNameChars);
    }

    public String buildFieldSetter(String fieldName) {
        char[] fieldNameChars = fieldName.toCharArray();
        fieldNameChars[0] = Character.toUpperCase(fieldNameChars[0]);

        return "set" + new String(fieldNameChars);
    }

    public Object getObjectValue(Object object, String fieldName) {
        boolean instantiateNullObjects = true;

        Object ret = null;
        Object targetObj = object;

        // if no delim
        if (fieldName.indexOf(".") == -1) {
            try {
                ret = targetObj.getClass().getMethod(buildFieldGetter(fieldName), null).invoke(targetObj, null);
            } catch (InvocationTargetException e) {
                log.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage(), e);
            }
        } else {
            // if have delim
            // get tokens
            List<String> tokens = new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(fieldName, ".");
            while (st.hasMoreTokens()) {
                tokens.add(st.nextToken());
            }

            try {
                // go thru' tokens and get object value
                for (Iterator<String> itr = tokens.iterator(); itr.hasNext();) {
                    String token = itr.next();
                    Object objValue = targetObj.getClass().getMethod(buildFieldGetter(token), null).invoke(targetObj, null);

                    if (instantiateNullObjects && objValue == null && itr.hasNext()) { // do not perform for LAST
                        objValue = targetObj.getClass().getDeclaredField(token).getType().newInstance();
                        targetObj.getClass().getMethod(buildFieldSetter(token), objValue.getClass()).invoke(targetObj, objValue);
                    }

                    // if object value not null
                    if (objValue != null) {
                        // if have next, then reassign target object
                        if (itr.hasNext()) {
                            targetObj = objValue;
                        } else {
                            ret = objValue;
                        }
                    }
                }

                tokens = null;
            } catch (InstantiationException e) {
                log.error(e.getMessage(), e);
            } catch (NoSuchFieldException e) {
                log.error(e.getMessage(), e);
            } catch (InvocationTargetException e) {
                log.error(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                log.error(e.getMessage(), e);
            } catch (NoSuchMethodException e) {
                log.error(e.getMessage(), e);
            }
        }

        return ret;
    }

    public String formatTransactionCode(String prefix, int number, LocalDateTime now) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddhhmm");
        StringBuilder sb = new StringBuilder(prefix);
        sb.append(now.format(formatter));
        sb.append(String.format("%04d", number));
        return sb.toString();
    }

    public <T> boolean contains(T value, T[] list) {
        for (T item : list) {
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public <T> Map<String, Class<?>> getEnumFieldList(Class<T> clazz) {
        Map<String, Class<?>> enumFieldList = new HashMap<String, Class<?>>();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                if (field.getType().isEnum()) {
                    enumFieldList.put(field.getName(), field.getType());
                }
            }
        }
        return enumFieldList;
    }
}
