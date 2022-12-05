package Backend;

import java.util.HashMap;
import java.util.List;

public interface TimelineCallback {
    void onCallback(HashMap<Integer, HashMap<Integer, List<String>>> callback);
}
