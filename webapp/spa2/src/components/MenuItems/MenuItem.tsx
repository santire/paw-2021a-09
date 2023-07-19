import { CloseButton, Group } from "@mantine/core";
import { useDeleteMenuItem } from "../../hooks/menuItems.hooks";
import { IMenuItem } from "../../types/menuItem/menuItem.models";

interface MenuItemProps {
  item: IMenuItem;
  restaurantId: number;
  isOwner?: boolean;
}
export function MenuItem({ item, restaurantId, isOwner }: MenuItemProps) {
  const deleteItem = useDeleteMenuItem();

  return (
    <tr key={item.id}>
      <td>{item.name}</td>
      <td>{item.description}</td>
      <td>{item.price}</td>
      <td>
        {isOwner ? (
          <Group position="center">
            <CloseButton
              aria-label="Close modal"
              onClick={() => {
                deleteItem.mutate({ restaurantId, menuItemId: item.id });
              }}
              disabled={deleteItem.isLoading}
            />
          </Group>
        ) : null}
      </td>
    </tr>
  );
}
