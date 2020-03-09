package matej.jamb.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/papers")
public class PaperController {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@GetMapping("/{id}/option/{option}")
    public void selectOption(@PathVariable(value="option") int option, @PathVariable(value="id") int id) {
		System.out.println("radim " + option + " na papiru " + id);
		List<Map<String, Object>> moveNumList = jdbcTemplate.queryForList("SELECT move_num FROM paper WHERE id = " + id);
		int moveNum = (int) moveNumList.get(id-1).get("move_num");
		jdbcTemplate.execute("UPDATE option SET option = " + option + ", move_num = " + moveNum + " WHERE paper_id = " + id);
		
    }

	@GetMapping("/{id}/print")
	public String getPaperPrintById(@PathVariable(value="id") int id) {
		List<Map<String, Object>> rowList = jdbcTemplate.queryForList("SELECT * FROM row WHERE paper_id = " + id);
		List<Map<String, Object>> boxList = new ArrayList<>();
		for (Map<String, Object> rowMap : rowList) {
			boxList.addAll(jdbcTemplate.queryForList("SELECT * FROM box WHERE row_id = " + rowMap.get("id")));
		}
//		System.out.println(boxList);
		String dashes = "\n-------------------";
		String string = dashes + 
				"\n |    A    |A    a" + 
				"\n V    |    V|    n"+
				dashes;
		int value;
		int score = 0;
		for (int i = 0; i < 13; i++) {
			string += "\n";
			for (Map<String, Object> rowMap : rowList) {
				for (Map<String, Object> boxMap : boxList) {
					if (boxMap.get("row_id") == rowMap.get("id") && (int) boxMap.get("box_number") == i) {
						if (!((boolean) boxMap.get("written"))) string += "|--| ";
						else {
							value = (int) boxMap.get("value");
							string += (value >= 0 && value <= 9 ? "| " : "|") + value + "| ";
						}
					}
				}
			}
			if (i == 5 || i == 7 || i == 12) {
				string += dashes + "\n";
				value = 0;
				for (Map<String, Object> rowMap : rowList) {
					if (i == 5) {
						value = (int) rowMap.get("upper_score");
						score += value;
					}  else if (i == 7) {
						value = (int) rowMap.get("middle_score");
						score += value;
					} else if (i == 12) {
						value = (int) rowMap.get("lower_score");
						score += value;
					}
					string += (value >= 0 && value <= 9 ? "| " : "|") + value + "| ";
				}
				string += dashes;
			}
		}
		return string + "\nScore: " + score + "\n";
	}
}
