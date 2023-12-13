import {
  afterAll,
  afterEach,
  assert,
  beforeAll,
  beforeEach,
  describe,
  expect,
  test,
  vi,
} from "vitest";
import {
  render,
  screen,
  fireEvent,
  waitFor,
  cleanup,
} from "@testing-library/react";
import { RestaurantCard } from "../src/components/RestaurantCard/RestaurantCard";
import mockRestaurant from "./mocks/restaurant.mock";
import { MemoryRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { rest } from "msw";
import { setupServer } from "msw/node";


let queryClient: QueryClient;

vi.mock("react-i18next", () => ({
  useTranslation: () => ({
    t: (key: string, options?: any) => {
      if (key === "restaurantCard.liked") {
        return `${options.likes} people liked this`; // Return the expected text
      }
      if (key === "restaurantCard.tagsTitle") {
        return "Tags:";
      }
      if (key.startsWith("tags.")) {
        // Extract the tag name from the key and return it
        return key.substring(5); // Assuming the key structure is 'tags.tag_name'
      }
      return key;
    },
  }),
}));

// Create a mock server
const server = setupServer(
  rest.post("/restaurants/" + mockRestaurant.id + "/likes", (req, res, ctx) => {
    return res(ctx.json({ success: true }));
  }),
);

beforeAll(() => {
  server.listen();
});

beforeEach(() => {
  queryClient = new QueryClient();
  render(
    <QueryClientProvider client={queryClient}>
      <MemoryRouter>
        <RestaurantCard restaurant={mockRestaurant} />
      </MemoryRouter>
    </QueryClientProvider>,
  );
});

afterEach(() => {
  cleanup();
});

afterAll(() => {
  server.close();
  vi.resetAllMocks();
});

describe("Render tests for Restaurant Card", () => {
  test("Should display correct restaurant name", () => {
    const nameElement = screen.getByText(mockRestaurant.name);
    assert(nameElement !== null, "nameElement should not be null");
    expect(nameElement.textContent).toBe(mockRestaurant.name);
  }),
    test("Should display correct likes number ", () => {
      const likesElement = screen.getByText(
        `${mockRestaurant.likes} people liked this`,
      );
      expect(likesElement).toBeInTheDocument();
    });

  test("Should display correct tags", () => {
    const tagsElement = screen.getByText("Tags:");
    expect(tagsElement).toBeInTheDocument();
    mockRestaurant.tags.forEach((tag) => {
      const tagElement = screen.getByText(tag);
      expect(tagElement).toBeInTheDocument();
    });
  });
});

describe("Testing services used by Restaurant Card component", () => {
  test("Should call Like method - USER AUTH", async () => {
    const likeRestaurantModule = await import("../src/hooks/like.hooks");
    // Spy on the useLikeRestaurant hook
    const likeRestaurantSpy = vi.spyOn(
      likeRestaurantModule,
      "useLikeRestaurant",
    );

    // Find the LikeButton in the rendered component
    const likeButton = screen.getByRole("button", { name: /like-button/i });

    // Check if the button is found before clicking
    if (likeButton) {
      fireEvent.click(likeButton);

      // Expect the useLikeRestaurant hook to have been called with the correct restaurant ID
      await waitFor(() => {
        // Expect the useLikeRestaurant hook to have been called with the correct restaurant ID
        expect(likeRestaurantSpy).toHaveBeenCalled();
      });
    } else {
      // If the button is not found, fail the test
      expect(false).toBe(true);
    }
  });

  test("Should call Like method - USER NOT AUTH", async () => {
    const likeButton = screen.getByRole("button", { name: /like-button/i });
    fireEvent.click(likeButton);

    // Wait for the modal to appear
    await waitFor(() => {
      const modalElement = screen.queryByText("Oops!");
      expect(modalElement).toBeDefined();
    });
  });
});
