import java.util.HashMap;

public abstract class AbstractPersistanceMapper implements IMapper {
	
	HashMap<String, PersistentObject> cachedObjects = new HashMap<String,PersistentObject>();
	
	public final Object get(String oid){
		Object obj = cachedObjects.get(oid);
		
		if(obj==null){
			
			obj = getObjectFromStorage(oid);
			cachedObjects.put(oid,(PersistentObject)obj);
		}
		return obj;
		
	}
	
	public final void put(String oid,Object obj){
		this.putObjectInStrorage(oid,obj);
	}
	
	protected abstract Object getObjectFromStorage(String oid);	
	
	protected abstract void putObjectInStrorage(String oid,Object obj);
}
