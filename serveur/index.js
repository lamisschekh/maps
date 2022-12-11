const {Server}=require ("socket.io")
const express = require ("express") 
const http = require("http")
const app = express ()
const ip = require("ip")


const server = http.createServer(app)
const io= new Server (server,{
    cors:{
        origin:"*"
    }
})

io.on("connection",(socket )=>{ 
    console.log(socket.id + " connected")
    socket.on("message",( message)=>{
        console.log(message)
        io.emit("message",message)
        
    })

})

server.listen(3001, "0.0.0.0",() => {
    console.log("Server is listening")
    console.log("Ip :" + ip.address());
})

