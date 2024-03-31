import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;
public class ToDoSpark {
    // Classe de modelo para um item
    public static class Item {
        int id;
        String nome;

        public Item(int id, String nome) {
            this.id = id;
            this.nome = nome;
        }
    }

    // Usaremos um array para simular um banco de dados simples
    static List<Item> itens = new ArrayList<>();
    static int proximoId = 1;
    public static void main(String[] args) {

        get("/crud", (req, res) -> "crud spark framework 2024");

        // Rota para listar todos os itens
        get("/items", (req, res) -> {
            res.type("application/json");
            return new Gson().toJson(itens);
        });

        // Rota para obter um item específico
        get("/items/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Item item = encontrarItem(id);
            if (item != null) {
                res.type("application/json");
                return new Gson().toJson(item);
            } else {
                res.status(404);
                return "Item não encontrado";
            }
        });

        // Rota para adicionar um novo item
        post("/items", (req, res) -> {
            String body = req.body();
            Item novoItem = new Gson().fromJson(body, Item.class);
            novoItem.id = proximoId++;
            itens.add(novoItem);
            res.status(201); // Código de status 201 para recurso criado
            return "Novo item adicionado com ID " + novoItem.id;
        });

        // Rota para atualizar um item existente
        put("/items/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            String body = req.body();
            Item itemAtualizado = new Gson().fromJson(body, Item.class);
            Item itemExistente = encontrarItem(id);
            if (itemExistente != null) {
                itemExistente.nome = itemAtualizado.nome;
                return "Item com ID " + id + " atualizado";
            } else {
                res.status(404);
                return "Item não encontrado";
            }
        });

        // Rota para excluir um item
        delete("/items/:id", (req, res) -> {
            int id = Integer.parseInt(req.params(":id"));
            Item item = encontrarItem(id);
            if (item != null) {
                itens.remove(item);
                return "Item com ID " + id + " excluído";
            } else {
                res.status(404);
                return "Item não encontrado";
            }
        });

    }

    // Método auxiliar para encontrar um item pelo ID
    static Item encontrarItem(int id) {
        for (Item item : itens) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }
}