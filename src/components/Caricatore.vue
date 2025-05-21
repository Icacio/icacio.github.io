<script setup>
import {onMounted, ref, onBeforeUnmount} from "vue";

const loading = ref("Conectando con dropbox");
const emit = defineEmits(["songsLoaded"]);
    
const options = {
	success: function(files) {
        emit("songsLoaded", files);
	},
	linkType: "direct",
	multiselect: true,
	extensions: ['audio'],
};
const checkDropboxLoaded = null;
const dropboxError = ref(false);

onMounted(()=> {
    let dropboxTimeout = setTimeout(() => {
        if (typeof Dropbox === 'undefined') {
			setLocal();
        }
    }, 5000); // 5 secondi di timeout
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
        <input type="text" id="folder"/>
	</div>
	<div>
		<button id="dropboxbutton">{{ loading }}</button>
	</div>
</template>