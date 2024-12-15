<script setup>
import MediaController from './components/MediaController.vue'
import {ref, onMounted} from "vue";

const song = ref(null);
let songs = null;

let index = 0;
const isPlaying = ref(false);
const duration = ref(1);

const options = {
	success: function(files) {
		songs = files;
		song.value = files[0].link;
		let audio = document.getElementById("audio");
		if (audio) {
			duration.value = audio.duration;
		}
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
	script.onload = () => {
		var button = Dropbox.createChooseButton(options);
		document.getElementById("dropboxbutton").replaceWith(button);
	}
	document.body.appendChild(script);
});

function setSong(address,i) {
	let audio = document.getElementById("audio");
	audio.pause();
	song.value = address;
	audio.load();
	audio.play();
	index = i;
	duration.value = audio.duration;
}

function songPaused() {
	isPlaying.value = false;
}

function songPlayed() {
	isPlaying.value = true;
}
function songNext() {
	let audio = document.getElementById("audio");
	audio.pause();
	song.value = songs[++index].link;
	audio.load();
	audio.play();
	duration.value = audio.duration;
}

function songPrevious() {
	let audio = document.getElementById("audio");
	audio.pause();
	song.value = songs[--index].link;
	audio.load();
	audio.play();
	duration.value = audio.duration;
}

</script>

<template>
	<div class="left">
		<p v-for="(song,index) in songs">
			<button @click="setSong(song.link,index)">
				{{ song.name }}
			</button>	
		</p>
	</div>
	<h1>IctoPlayer</h1>
	<p>
		<template v-if="song">
			<audio id="audio"
			@play="songPlayed"
			@pause="songPaused"
			@ended="songNext">
				<source :src="song" type="audio/mpeg">
			</audio>
			{{ songs[index].name }}
			<MediaController v-model="isPlaying" :nextFunction="songNext" :previousFunction="songPrevious" :time=duration />
		</template>
		<div v-else>
			<button id="dropboxbutton">Esperando a dropbox</button>
		</div>
	</p>
</template>

<style>
.left {
	position: fixed;
    top: 0;
    left: 0;
    width: 200px;
    height: 100%;
    background-color: white;
    overflow-y: auto;
}
</style>