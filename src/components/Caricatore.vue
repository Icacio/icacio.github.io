<script setup>
import {onMounted, ref, onBeforeUnmount} from "vue";

const loading = ref("Conectando con dropbox");
const emit = defineEmits(["loadMusic"]);
const folderName = ref("http://localhost:8000/");
    
const options = {
	success: function(files) {
        emit("loadMusic", files);
	},
	linkType: "direct",
	multiselect: true,
	extensions: ['audio'],
};
let checkDropboxLoaded;
const dropboxError = ref(false);

onMounted(()=> {
    let dropboxTimeout = setTimeout(() => {
        if (typeof Dropbox === 'undefined') {
			setLocal();
        }
    }, 5000);
	checkDropboxLoaded = setInterval(() => {
		if (typeof Dropbox !== 'undefined') {
			clearInterval(checkDropboxLoaded);
			var button = Dropbox.createChooseButton(options);
			document.getElementById("dropboxbutton").replaceWith(button);
		}
		switch(loading.value){
			case "Conectando con dropbox     ":
				loading.value = "Conectando con dropbox.    ";
				break;
			case "Conectando con dropbox.    ":
				loading.value = "Conectando con dropbox..   ";
				break;
			case "Conectando con dropbox..   ":
				loading.value = "Conectando con dropbox...  ";
				break;
			default:
				loading.value = "Conectando con dropbox     ";
		}
	}, 500);
});

onBeforeUnmount(() => {
    if (checkDropboxLoaded) clearInterval(checkDropboxLoaded);
});

function loadLocal() {
	try {
		fetch(folderName.value+".json")
			.then(response => {
				const contentType = response.headers.get("content-type");
				if (contentType && contentType.includes("application/json")) {
					return response.json();
				} else {
					throw new Error("Invalid JSON file");
				}}).then(data => {
					if (data && Array.isArray(data)) {
						emit("loadMusic",data);
					} else {
						console,error("Invalid JSON format");
					}
				})
			.catch(error => {
				console.error("Error loading songs:", error);
			});
	}
	catch (e) {
		console.error(e);
	}
}

function setLocal() {
	dropboxError.value = true;
	clearInterval(checkDropboxLoaded);
	let bu = document.getElementById("dropboxbutton")
	if (bu) {
		bu.addEventListener("click", async (e) => {
			e.preventDefault();
			let folder = document.getElementById("folder").value;
			if (folder) {
			}
		})
	}
}
</script>
<template>
	<div v-if="dropboxError">
		<p>Error de conexión a Dropbox</p>
		<p>Modo local activado</p>
		<form @submit.prevent="loadLocal">
			<input type="text" v-model="folderName">
			<button type="submit">Load</button>
		</form>
	</div>
	<div v-else>
		<p>Selecciona una carpeta de Dropbox</p>
		<button id="dropboxbutton">{{ loading }}</button>
		<br>
		<hr/>
		<p>O usa el modo local</p>
		<button @click="setLocal">Modo local</button>
	</div>
</template>