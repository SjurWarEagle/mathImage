const PROXY_CONFIG = [
  {
    context: [
      "/api/"
    ],
    target: "http://localhost:9292/",
    changeOrigin: true,
    secure: false
  }
];

module.exports = PROXY_CONFIG;
