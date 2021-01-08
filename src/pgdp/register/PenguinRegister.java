package pgdp.register;

import com.sun.jdi.CharType;

import static java.lang.String.valueOf;

public class PenguinRegister {
    // Constants
    private static final char FIRST_CHAR = 'A', LAST_CHAR = 'Z';
    //nur Großbuchstaben
    // TODO: set back to private
    private static final int NUM_ALPHABET = LAST_CHAR - FIRST_CHAR + 1;
    private  Penguin  penguin = null;
    private PenguinRegister[] children = new PenguinRegister[26];

    Penguin getPenguin() {
        return penguin;
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
            if(node.children[intCurrentChar]==null)node.children[intCurrentChar] = new PenguinRegister();
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
            if(node.children[intCurrentChar]==null) return null;
            else {
                node =(PenguinRegister) node.children[intCurrentChar];
            }

        }
        return node.getPenguin();
    }

    //wenn get null dann false
   public boolean contains(String name) {
        name= name.toUpperCase();
        return (get(name)!=null);
    }

    private int sizeHelper(PenguinRegister currentRegister) {
        int grose = 0;
        if (currentRegister.getPenguin() != null) grose++;
        for (PenguinRegister r : currentRegister.children) {
            if(r!=null) grose += sizeHelper(r);
        }
        return grose;
    }
    //rekursiv
    public int size() {
        return sizeHelper(this);
    }

    String findNameHelper(PenguinRegister register,String name,Penguin pengu){
        if (register.getPenguin() == pengu) return name;
        for (int i =0;i<=25;i++) {
            PenguinRegister r = register.children[i];
            if(r!=null) findNameHelper(r,name+((char)('A'+i)),pengu);
        }
        return name;
    }
    String findNameHelper2(PenguinRegister register,Penguin pengu) {
        if (register.getPenguin() == pengu) return "";
        for (int i = 0; i <= 25; i++) {
            PenguinRegister r = register.children[i];
            if (r != null) {
                String s = findNameHelper2(r, pengu);
                if (s != null) return ((char) ('A' + i)) + s;
            }

        }
        return null;
    }
    //rekursiv 1:Pingu finden; dann rekonstruieren
    String findName(Penguin penguin) {
        return findNameHelper2(this,penguin);
    }

    Penguin removeHelper(PenguinRegister register,String name) {

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
            if(register.children[i]==null)return;
            else if (register.children[i].size() == 0) register.children[i]=null;
            else removeHelper2(register.children[i]);
        }
    }

    Penguin remove(String name) {
        Penguin r =removeHelper(this,name);
        removeHelper2(this);
        return r;

    }
    EntryList getAllHelper(PenguinRegister register, EntryList Elist){
        if (register.getPenguin() != null) {
            Penguin r = register.getPenguin();
            String name = findName(r);
            Entry entry = new Entry(name,r);
            Elist.add(entry);
        }
        for (PenguinRegister r : register.children) {
            if (r != null) {
                EntryList t = getAllHelper(r, Elist);
                for (Entry e : t) {
                    Elist.add(e);
                }
            }
        }
        return Elist;
    }
    EntryList getAll() {
        EntryList Elist = new EntryList();
        return getAllHelper(this, Elist);
    }
    PenguinRegister prefixHelper(PenguinRegister register, String prefix) {
        if (prefix.isEmpty()) return register;
        else {
            PenguinRegister r = register.children[prefix.charAt(0) - 'A'];
            if (r == null) return null;
            return r.prefixHelper(r, prefix.substring(1));

        }
    }

    EntryList getAllWithPrefix(String prefix){
        return prefixHelper(this,prefix).getAll();
    }
    EntryList tinder(PenguinRegister register,String pattern) {
        EntryList Elist = new EntryList();
        if (pattern.isEmpty() && register.getPenguin() != null)
            Elist.add(new Entry(findName(register.getPenguin()), register.getPenguin()));
        for (PenguinRegister r : register.children) {
            if (r != null) {
                for (Entry i : r.tinder(register, pattern)) {
                    Elist.add(i);

                }

            }
        }
        return Elist;
    }
    public EntryList getAllMatching(String pattern){
        return tinder(this, pattern);
    }
}

