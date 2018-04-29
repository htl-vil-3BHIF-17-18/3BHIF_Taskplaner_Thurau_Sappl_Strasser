package dal;

import java.util.Map;
import java.util.Set;

public interface DatabaseMapable {

	@Deprecated
	public Map<String, String> columnFieldMap();
	
	public Set<String> databaseColumns();
	
	public Set<String> databaseValues();
}
