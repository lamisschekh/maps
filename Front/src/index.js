
const PARIS_POSITION1 = {lng: 2.320041, lat: 48.8588897}
const PARIS_POSITION2 = {lng: 2.330041, lat: 48.8588897}
const PARIS_POSITION3 = {lng: 2.340041, lat: 48.8588897}
const User_position={ lng: 2.310041, lat: 48.8588897}
const ARRIVAL={lng: 2.300041, lat: 48.8588897}

const USER_MARKER_ICON =L.divIcon({
    html:`<svg width="25" height="36" viewBox="0 0 25 36" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M12.75 0.699997C5.995 0.699997 0.5 6.195 0.5 12.95C0.5 22.1375 12.75 35.7 12.75 35.7C12.75 35.7 25 22.1375 25 12.95C25 6.195 19.505 0.699997 12.75 0.699997ZM12.75 4.2C14.675 4.2 16.25 5.775 16.25 7.7C16.25 9.6425 14.675 11.2 12.75 11.2C10.825 11.2 9.25 9.6425 9.25 7.7C9.25 5.775 10.825 4.2 12.75 4.2ZM12.75 21.7C9.8275 21.7 7.255 20.2125 5.75 17.9375C5.785 15.6275 10.4225 14.35 12.75 14.35C15.0775 14.35 19.715 15.6275 19.75 17.9375C18.245 20.2125 15.6725 21.7 12.75 21.7V21.7Z" 
    fill="green"/>
    </svg>`
})

const DESTINATION_MARKER =L.divIcon({
    html:`<svg width="25" height="36" viewBox="0 0 25 36" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M12.75 0.699997C5.995 0.699997 0.5 6.195 0.5 12.95C0.5 22.1375 12.75 35.7 12.75 35.7C12.75 35.7 25 22.1375 25 12.95C25 6.195 19.505 0.699997 12.75 0.699997ZM12.75 4.2C14.675 4.2 16.25 5.775 16.25 7.7C16.25 9.6425 14.675 11.2 12.75 11.2C10.825 11.2 9.25 9.6425 9.25 7.7C9.25 5.775 10.825 4.2 12.75 4.2ZM12.75 21.7C9.8275 21.7 7.255 20.2125 5.75 17.9375C5.785 15.6275 10.4225 14.35 12.75 14.35C15.0775 14.35 19.715 15.6275 19.75 17.9375C18.245 20.2125 15.6725 21.7 12.75 21.7V21.7Z" 
    fill="red"/>
    </svg>`
})




const restaurants= [
    {
        name:"restau 1",
        lat:PARIS_POSITION1.lat,
        lng:PARIS_POSITION1.lng
    },
    {
        name:"restau 2",
        lat:PARIS_POSITION2.lat,
        lng:PARIS_POSITION2.lng
    },
    {
        name:"restau 3",
        lat:PARIS_POSITION3.lat,
        lng:PARIS_POSITION3.lng
    }
]
const users =[
    {
        name:"user1",
        lat:User_position.lat,
        lng:User_position.lng
    }

]



const mapContainer =document.querySelector("#map")
const map= L.map(mapContainer).setView(PARIS_POSITION1,14)
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 19,
      attribution: '&copy; <a href="https://openstreetmap.org/copyright%22%3EOpenStreetMap contributors</a>'
    }).addTo(map);


restaurants.forEach(function(data) {
    addMarker(data)
})
users.forEach(function(data){
    addMarker(data,USER_MARKER_ICON)
})

addMarker(ARRIVAL,DESTINATION_MARKER)


function addMarker (data,icon){

    const params={}

    if (icon){
        params.icon= icon
    }
    if(icon==USER_MARKER_ICON || icon==DESTINATION_MARKER){
        params.draggable=true
    }



    const marker= L.marker([data.lat,data.lng],params)
    marker.addTo(map)

}

var latlngs = 
              [
                [PARIS_POSITION1.lat,PARIS_POSITION1.lng ],
              //[PARIS_POSITION2.lat, PARIS_POSITION2.lng],
              //[PARIS_POSITION3.lat, PARIS_POSITION3.lng],
              [ARRIVAL.lat,ARRIVAL.lng],

              [User_position.lat,User_position.lng]
              
            ] 



            var polyline = L.polyline(latlngs, {color: 'red'}).addTo(map);





