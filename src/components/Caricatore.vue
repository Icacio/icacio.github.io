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
let checkDropboxLoaded;

onMounted(()=> {
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



</script>
<template>
	<div>
		<button id="dropboxbutton">{{ loading }}</button>
	</div>
</template>