package asgn2;
import java.util.Map;
import java.util.TreeMap;
//Student: Gulbanu Madiyarova

public final class UserManager {

    private static UserManager instance;
    private TreeMap<String, User> users = new TreeMap<>();
    private UserManager() {}

    public static synchronized UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void init(String[] usersIn) throws InvalidInputException, DataValidationException {
        for (String s : usersIn) {
            if (s == null) {
                throw new DataValidationException( "Bad data entered");
            } else {
                User createdUser = new User(s);
                this.users.put(s, createdUser);
            }
        }
    }

    public void updateTradable(String userId, TradableDTO o) throws DataValidationException, InvalidInputException {
        if (userId == null || o == null) {
            throw new DataValidationException( "Data Validation failed");
        } else {
            User createdU = new User(userId);
            if (!users.containsKey(userId)) {
                users.put(userId, createdU);
            }
            this.users.get(userId).updateTradable(o);
        }
    }

    @Override
    //TODO check if this is printing correctly
    public String toString() {
        StringBuilder string = new StringBuilder();
        for(Map.Entry<String, User> entry : users.entrySet()){
            string.append(entry.getValue().toString());
        }
        return string.toString();
    }

    public User getUser(String userId) throws NotFoundException {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User not found: " + userId);
        } else {
            return users.get(userId);
        }
    }
}
