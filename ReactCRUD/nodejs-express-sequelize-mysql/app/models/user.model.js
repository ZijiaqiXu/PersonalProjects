module.exports = (sequelize, Sequelize) => {
    const User = sequelize.define("user", {
      name: {
        type: Sequelize.STRING
      },
      employeeId: {
        type: Sequelize.STRING
      },
      title: {
        type: Sequelize.STRING
      }
    });
  
    return User;
  };