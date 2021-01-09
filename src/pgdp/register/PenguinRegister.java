package pgdp.register;

import com.sun.jdi.CharType;

import static java.lang.String.valueOf;

public class PenguinRegister {
    // Constants
    private static final char FIRST_CHAR = 'A', LAST_CHAR = 'Z';
    //nur Großbuchstaben
    private static final int NUM_ALPHABET = LAST_CHAR - FIRST_CHAR + 1;
    private Penguin penguin = null;
    private PenguinRegister[] children = new PenguinRegister[26];

    Penguin getPenguin() {
        return penguin;
    }

    int[] findIndex(PenguinRegister register) {
        int resultcounter = 0;
        int counter = 0;
        for (int i = 0; i < 25; i++) {
            if (register.children[i] != null) resultcounter++;
        }
        int[] temp = new int[resultcounter];
        for (int i = 0; i < 25; i++) {
            if (register.children[i] != null) {
                temp[counter] = i;
                counter++;
            }
        }
        return temp;

    }


    void setPenguin(Penguin penguin) {
        this.penguin = penguin;
    }

    public boolean isEpmty() {
        boolean boo = true;
        for (int i = 0; i <= 26; i++) {
            if (children[i] != null)
                boo = false;
        }
        return boo;
    }


    public PenguinRegister put(String name, Penguin penguin) {
        name = name.toUpperCase();
        PenguinRegister node = this;
        for (int i = 1; i <= name.length(); i++) {
            //berechnung des zugehörigen Arrays
            int intCurrentChar = name.charAt(i - 1) - 'A';
            String strgCurrChar = valueOf((char) intCurrentChar);
            if (node.children[intCurrentChar] == null) node.children[intCurrentChar] = new PenguinRegister();
            if (i == name.length()) {
                node.children[intCurrentChar].setPenguin(penguin);
            }
            node = node.children[intCurrentChar];

        }
        return node;
    }

    public Penguin get(String name) {
        PenguinRegister node = this;
        name = name.toUpperCase();
        for (int i = 0; i < name.length(); i++) {
            int intCurrentChar = name.charAt(i) - 'A';
            if (node.children[intCurrentChar] == null) return null;
            else {
                node = (PenguinRegister) node.children[intCurrentChar];
            }

        }
        return node.getPenguin();
    }

    //wenn get null dann false
    public boolean contains(String name) {
        name = name.toUpperCase();
        return (get(name) != null);
    }

    private int sizeHelper(PenguinRegister currentRegister) {
        int grose = 0;
        if (currentRegister.getPenguin() != null) grose++;
        for (PenguinRegister r : currentRegister.children) {
            if (r != null) grose += sizeHelper(r);
        }
        return grose;
    }

    //rekursiv
    public int size() {
        return sizeHelper(this);
    }

    Entry findNameHelper(PenguinRegister register, String name, Penguin pengu) {
        int index[] = findIndex(register);
        int indexLength = index.length;
        for (int i = 0; i < indexLength; i++) {
            PenguinRegister r = register.children[index[i]];
            if (r != null && register.getPenguin() == pengu) {
                return new Entry(name, register.getPenguin());
            }
            if (r != null) return findNameHelper(r, name + ((char) ('A' + index[i])), pengu);

        }
        return null;
    }

    String findNameHelper2(PenguinRegister register, Penguin pengu) {
        String s = "";
        if (register.getPenguin() == pengu) return "";
        for (int i = 0; i <= 25; i++) {
            PenguinRegister r = register.children[i];
            if (r != null) {
                s = findNameHelper2(r, pengu);
                if (s != null) return ("" + ((char) ('A' + i)) + s);
            }

        }
        return null;
    }


    //rekursiv 1:Pingu finden; dann rekonstruieren
    String findName(Penguin penguin) {

        return findNameHelper2(this, penguin);
    }

    String findPrefixName(Penguin penguin,String prefix,String fixedPrefix) {

        return fixedPrefix+findNameHelper2(this, penguin);
    }


    Penguin removeHelper(PenguinRegister register, String name) {

        if (name.isEmpty()) {
            Penguin r = register.getPenguin();
            register.setPenguin(null);
            return r;
        }
        if (register.children[name.charAt(0) - 'A'] != null)
            return removeHelper(register.children[name.charAt(0) - 'A'], name.substring(1));
        else return null;
    }

    void removeHelper2(PenguinRegister register) {
        // remove the paths leading to the removed penguin
        for (int i = 0; i <= 25; i++) {
            if (register.children[i] == null) return;
            else if (register.children[i].size() == 0) register.children[i] = null;
            else removeHelper2(register.children[i]);
        }
    }

    Penguin remove(String name) {
        Penguin r = removeHelper(this, name);
        removeHelper2(this);
        return r;

    }

    EntryList getAllHelper(PenguinRegister register, EntryList Elist) {
        // return EntryList of all penguin in the tree
        if (register.getPenguin() != null) {
            Penguin r = register.getPenguin();
            String name = findName(r);
            Entry entry = new Entry(name, r);
            Elist.add(entry);
        }
        for (int i = 0; i < 26; i++) {
            if (register.children[i] != null) {
                getAllHelper(register.children[i], Elist);
            }
        }
        return Elist;
    }
    EntryList getAllPrefixHelper(PenguinRegister register, EntryList Elist,String prefix, String fixedPrefix) {
        // return EntryList of all penguin in the tree
        if (register.getPenguin() != null) {
            Penguin r = register.getPenguin();
            String name = findPrefixName(r,prefix,fixedPrefix);
            Entry entry = new Entry(name, r);
            Elist.add(entry);
        }
        for (int i = 0; i < 26; i++) {
            if (register.children[i] != null) {
                getAllHelper(register.children[i], Elist);
            }
        }
        return Elist;
    }

    EntryList getAll() {
        EntryList Elist = new EntryList();
        return getAllHelper(this, Elist);
    }
    EntryList getAllPrefix(String prefix,String fixedPrefix) {
        EntryList Elist = new EntryList();
        return getAllPrefixHelper(this, Elist,prefix,fixedPrefix);
    }


    PenguinRegister prefixHelper(PenguinRegister register, String prefix,String fixedPrefix) {
        int index[] = findIndex(this);
        int lengthIndex = index.length;
        if (prefix.isEmpty()) return register;
        else {
            for (int i = 0; i < lengthIndex; i++) {
                if (index[i] == prefix.charAt(0) - 'A') {
                    PenguinRegister r = register.children[prefix.charAt(0) - 'A'];
                    if (r == null) return null;
                    else return r.prefixHelper(r, prefix.substring(1),fixedPrefix);
                }
            }
        }
        return this;
    }


    public EntryList getAllWithPrefix(String prefix) {
        EntryList Elist = new EntryList();

        prefix = prefix.toUpperCase();
        String fixedPrefix = prefix;
        if (prefixHelper(this, prefix,fixedPrefix)== null) return null;
        return prefixHelper(this, prefix,fixedPrefix).getAll();
    }
    //retunred Entrylist
    EntryList tinderGetAll(){
        EntryList Elist= new EntryList();
        return getAllTinderHelper(this, Elist);

    }
    //makes an Entry if register has a Pengu
    EntryList getAllTinderHelper(PenguinRegister register,EntryList Elist){
        if (register.getPenguin() != null) {
            Penguin r = register.getPenguin();
            String name = findName(r);
            Entry entry = new Entry(name, r);
            Elist.add(entry);
        }
        return Elist;
    }
    //
    EntryList tinder2(PenguinRegister register,String pattern){
        EntryList Elist = new EntryList();
        if(register==null) return Elist;
        else if(pattern=="") return getAllTinderHelper(this,Elist);
        else if(pattern.charAt(0)!='.'){
            if(register.children[pattern.charAt(0)-'A']!=null){
                if(register.children[pattern.charAt(0)-'A'].getPenguin()!=null) return getAllTinderHelper(this,Elist);
            }
        }
        return null;
    }

    EntryList tinder(PenguinRegister register, String pattern) {
        EntryList Elist = new EntryList();
        if (pattern.isEmpty() && register!= null)
            return this.getAll();
           // Elist.add(new Entry(findName(register.getPenguin()), register.getPenguin()));
        else if (pattern.charAt(0) == '.') {
            for (int i = 0; i < 26; i++) {
                if (register.children[i] != null) {
                    tinder(register.children[i],pattern.substring(1));
                }
            }
        }
        else {
            for (int i = 0; i < 26; i++) {
                int firstLetter = (pattern.charAt(0)) - 'A';
                if (register.children[firstLetter] != null) {
                    tinder(register.children[firstLetter], pattern.substring(1));
                }

            }
        }
        return Elist;
            }


    public EntryList getAllMatching(String pattern){
        if(pattern ==null || pattern=="")return new EntryList();
        return tinder(this, pattern);
    }

}

