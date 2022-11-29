package Backend;

import java.util.List;

//can be used for some tricky stuff sometimes
public interface GetProfileCallback {
    void onStudent(Student student);
    void onAdmin(Admin admin);
}
