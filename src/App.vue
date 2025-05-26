<script setup>
import MediaController from './components/MediaController.vue'
import Caricatore from './components/Caricatore.vue'
import {ref, useTemplateRef} from "vue";

const song = ref(null);
let songs = ref(null);

let index = 0;
const isPlaying = ref(false);
const duration = ref(1);
const audioTag = useTemplateRef("audioElement")
const currentTime = ref(0)




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
	song.value = songs.value[++index].link;
	audio.load();
	audio.play();
	setDuration();
}

function songPrevious() {
	let audio = audioTag.value;
	audio.pause();
	song.value = songs.value[--index].link;
	audio.load();
	audio.play();
	setDuration();
}

function songsLoaded(files) {
		console.log("songsLoaded");
		songs.value = files;
		song.value = files[0].link;
		let audio = document.getElementById("audio");
		if (audio) {
			duration.value = audio.duration;
		}
}
function shuffle() {
	const current = songs.value[index];
	songs.value.splice(index, 1);
	songs.value.sort(() => Math.random() - 0.5);
	songs.value.unshift(current);
	index = 0;
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
				:audioTag="audioTag" 
				:shuffle="shuffle"/>
		</template>
		<Caricatore @songsLoaded="songsLoaded" v-else />
	</p>
</template>