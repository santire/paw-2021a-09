import { apiClient, apiErrorHandler } from "../client";

const PATH = "/tags";

interface ITagService {
  getAll(): Promise<string[]>;
}

module TagServiceImpl {
  export async function getAll() {
    try {
      const response = await apiClient.get<string[]>(PATH);
      return response.data;
    } catch (error) {
      throw new Error("General Error", {
        cause: apiErrorHandler(error),
      });
    }
  }
}

const TagService: ITagService = TagServiceImpl;
export { TagService };
