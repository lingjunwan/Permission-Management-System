// Import components for production environments
module.exports = (file) => () => import("@/views/" + file + ".vue");
