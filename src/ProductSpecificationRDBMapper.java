import java.sql.ResultSet;
import java.sql.SQLException;


public class ProductSpecificationRDBMapper extends AbstractRDBMapper {

	
	public ProductSpecificationRDBMapper(String tableName) {
		super(tableName);		
		
	}
	
	@Override
	protected Object getObjectFromRecord(String id, ResultSet DBRecord) {
		String name = null;
		String price = null;
		String oid = null;
		
		try {
			while(DBRecord.next()){
				name = DBRecord.getString("name");
				oid = DBRecord.getString("id");
				price = DBRecord.getString("price");
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		if(id==null)
			return null;
		
		ProductSpecification ps = new ProductSpecification();
		
		
		try{
			
			ps.setId(Integer.parseInt(oid));
			ps.setName(name);
			ps.setPrice(Integer.parseInt(price));
		}catch(Exception e){
			e.printStackTrace();
		}
		return ps;
		
		
	}

	

	@Override
	protected void putObjectInStrorage(String oid,Object obj) {
		// TODO Auto-generated method stub
		
	}


	
}
