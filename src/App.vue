<script setup>
import MediaController from './components/MediaController.vue'
import {ref, onMounted, useTemplateRef} from "vue";

const song = ref(null);
let songs = null;

let index = 0;
const isPlaying = ref(false);
const duration = ref(1);
const audioTag = useTemplateRef("audioElement")
const currentTime = ref(0)

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

const updateTime = () => {
	if(audioTag.value) {
		currentTime.value = audioTag.value.currentTime;
	}
}
function setDuration() {
	if (audioTag.value) {
		duration.value = audioTag.value.duration;
	}
}
function setSong(address,i) {
	let audio = audioTag.value;
	audio.pause();
	song.value = address;
	audio.load();
	audio.play();
	index = i;
	setDuration();
}

function songPaused() {
	isPlaying.value = false;
}

function songPlayed() {
	isPlaying.value = true;
}
function songNext() {
	let audio = audioTag.value;
	audio.pause();
	song.value = songs[++index].link;
	audio.load();
	audio.play();
	setDuration();
}

function songPrevious() {
	let audio = audioTag.value;
	audio.pause();
	song.value = songs[--index].link;
	audio.load();
	audio.play();
	setDuration();
}

</script>

<template>
	<div v-if="songs" class="left">
		<p v-for="(song,index) in songs">
			<button @click="setSong(song.link,index)">
				{{ song.name }}
			</button>	
		</p>
	</div>
	<h1>IctoPlayer</h1>
	<p>
		<template v-if="song">
			<audio id="audio" preload="metadata"
				@play="songPlayed"
				@pause="songPaused"
				@ended="songNext"
				@timeupdate="updateTime"
				@loadedmetadata="setDuration"
				ref="audioElement">
					<source :src="song" type="audio/mpeg">
			</audio>
			{{ songs[index].name }} <br>
			<MediaController v-if="audioTag" v-model="isPlaying"
				:nextFunction="songNext"
				:previousFunction="songPrevious"
				:time=duration
				:currentTime="currentTime"
				:audioTag="audioTag" />
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