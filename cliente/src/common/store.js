import { ref } from "vue";

const store = ref({
  state: {
    user: {
      autoridad: "",
      login: "",
      logged: false,
    },
  },
});

export { getStore };

function getStore() {
  return store.value;
}
