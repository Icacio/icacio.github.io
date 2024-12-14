<script setup>
import MediaController from './components/MediaController.vue'
import {ref, onMounted} from "vue";

let song = ref(null);
let songs = null;

const options = {
	success: function(files) {
		song.value = files[0].link;
		console.log("ran");
	},
	linkType: "direct",
	multiselect: true,
	extensions: ['audio'],
};

onMounted(()=> {
	const script = document.createElement('script');
	script.src = 'https://www.dropbox.com/static/api/2/dropins.js';
	script.id = 'dropboxjs';
	script.setAttribute('data-app-key', '3p019xr3e3jhfqb');
	script.onload = ()=> {
		var button = Dropbox.createChooseButton(options);
		document.getElementById("dropboxbutton").replaceWith(button);
	}
	document.body.appendChild(script);
});
	
</script>

<template>
	<h1>IctoPlayer</h1>
	<p>
		<audio v-if="song" id="myAudio">
			<source :src="song" type="audio/mpeg">
			<MediaController/>
		</audio>
		<div v-else>
			<p>Inicia sesión en Dropbox</p>
			<button id="dropboxbutton"></button>
		</div>
		<br><br>
	</p>
</template>