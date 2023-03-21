public class BinarySearchTree2 {
    public class Node {
        public Comparable data;
        public Node left, right;
        public Node (Comparable data) { this (data, null, null); }
        public Node (Comparable data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }

    private Node root;

    public BinarySearchTree2(){
        root = null;
    }

    private Node find(Comparable key){
        Node current = root;
        if (current == null){
            return null;
        }
        while (current.data.compareTo(key) != 0){

            if(current.data.compareTo(key) > 0 ){
                current = current.left;
            }
            if(current.data.compareTo(key) < 0 ){
                current = current.right ;
            }
            if (current == null){
                return null;
            }
        }
        return current;
    }

    private Node findRec (Comparable key) {
        return findHelper (key, root);
    }

    private Node findHelper(Comparable key, Node current){
        if (current == null)
            return null;
        if (current.data.compareTo(key) == 0){
            return current;
        }
        if (current.data.compareTo(key) > 0){
            return findHelper(key, current.left);
        }
        else
        {
            return findHelper(key, current.right);
        }
    }

    public void add(Comparable key){
        Node current = root;
        Node parent = null;

        while (current != null){
            if (current.data.compareTo(key) == 0){
                return;
            }
            if (current.data.compareTo(key) > 0){
                parent = current;
                current = current.left;
            }
            else{
                parent = current;
                current = current.right;
            }
        }
        if(parent == null){
            root = new Node(key);
        }
        else{
            if(parent.data.compareTo(key) > 0){
                    parent.left = new Node(key);
            }else{
                parent.right = new Node(key);
            }
        }
    }

    public Node delete(Comparable key){
        if(root == null)
            return null;
        Node current = root;
        Node parent = root;
        Node substitute = null;
        boolean isRight = true;

        while(current.data.compareTo(key) != 0 ){
            if(current.data.compareTo(key) > 0){
                parent = current;
                current = current.left;
            }else{
                parent = current;
                current = current.right;
            }
            if (current == null){
                return null;
            }
        }

        if(current.left == null && current.right == null){
            substitute = null;
        }

        else if(current.left == null){
            substitute = current.right;
        }
        else if(current.right == null){
            substitute = current.left;
        }
        else{
            Node successor = current.right;
            Node successorParent = current;

            while (successor.left != null){
                successorParent = successor;
                successor = successor.left;
            }

            substitute = successor;

            if (successorParent != current) {
                successorParent.left = successor.right;
                substitute.right = current.right;
            }
            substitute.left = current.left;

        }
        if (current == root) root = substitute;
        else if (isRight) parent.right = substitute;
        else parent.left = substitute;
        return current;
    }

    public String TraversePreAll(){
        return TraversePre(root);
    }
    public String TraversePre(Node current){
        if (current == null)
            return "";
        return current.data + " " +  TraversePre(current.left) + TraversePre(current.right);
    }

    public String TraversePostAll(){
        return TraversePost(root);
    }

    public String TraversePost(Node current){
        if (current == null)
            return "";
        return  TraversePost(current.left) + TraversePost(current.right) + " " + current.data;
    }

    public String TraverseInAll(){
        return TraverseIn(root);
    }

    public String TraverseIn(Node current){
        if (current == null)
            return "";
        return  TraverseIn(current.left) + current.data + " " + TraverseIn(current.right);
    }
}

