import { CloseButton, Group } from "@mantine/core";
import { useDeleteMenuItem } from "@/hooks/menuItem.hooks";
import { IMenuItem } from "@/types/menuItem/menuItem.models";

interface MenuItemProps {
  item: IMenuItem;
  isOwner?: boolean;
}
export function MenuItem({ item, isOwner }: MenuItemProps) {
  const deleteItem = useDeleteMenuItem(item.restaurant);

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
                deleteItem.mutate({ url: item.self });
              }}
              disabled={deleteItem.isPending}
            />
          </Group>
        ) : null}
      </td>
    </tr>
  );
}
