public class ChangeEvent {
    public Song deletedFromFirstN;
    public Song addedToFirstN;
    ChangeEvent(){
        deletedFromFirstN = null;
        addedToFirstN = null;
        // ADD ONE FOR THE INDICES, IF YOU WANT TO USE THE SONGID AS THE INDEX
    }

    public String toString() {
        return "added: " + addedToFirstN + " deleted: " + deletedFromFirstN;
    }
}
