module.exports = app => {
    const users = require("../controllers/user.controller.js");
  
    var router = require("express").Router();
  
    // Create a new User
    router.post("/", users.create);
  
    // Retrieve all Users
    router.get("/", users.findAll);

    // Retrieve user by name
    router.get("/:name", users.findByName);

    // Retrieve all published Users
    router.get("/untitled", users.findAllUntitled);
  
    // Retrieve a single User with id
    router.get("/:id", users.findById);

    // Update a User with id
    router.put("/:id", users.update);
  
    // Delete a User with id
    router.delete("/:id", users.deleteById);
    
    // Delete a User with employee id
    router.delete("/:employeeId", users.deleteByEmployeeId);
  
    // Delete all Users
    router.delete("/", users.deleteAll);
  
    app.use('/api/users', router);
  };