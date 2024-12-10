import HTTP from "@/common/http";

export default {
    async getContent() {
        return (await HTTP.get("/about/1")).data;
    },

    async updateContent(payload) {
        const token = localStorage.getItem("token");
        return await HTTP.post("/about/update", payload, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
      },
};