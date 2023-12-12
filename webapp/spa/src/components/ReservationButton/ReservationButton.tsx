import { useUser } from "@/hooks/user.hooks";
import { Box, Text, Button, Center, Modal } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { ReservationModal } from "../ReservationModal/ReservationModal";
import { IRestaurant } from "@/types/restaurant/restaurant.models";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

interface Props {
  restaurant: IRestaurant;
}
export function ReservationButton({ restaurant }: Props) {
  const [showModal, setShowModal] = useState(false);
  const navigate = useNavigate();
  const { t } = useTranslation();
  const user = useUser();

  if (user.isSuccess) {
    return (
      <Box>
        <Button
          mt="xl"
          color="orange"
          variant="outline"
          size="xl"
          onClick={() => {
            setShowModal(true);
          }}
        >
          {t("pages.restaurant.reservationButton")}
        </Button>
        <ReservationModal
          restaurant={restaurant}
          user={user.data}
          show={showModal}
          setShow={setShowModal}
        />
      </Box>
    );
  }

  return (
    <Box>
      <Button
        mt="xl"
        color="orange"
        variant="outline"
        size="xl"
        onClick={() => {
          setShowModal(true);
        }}
      >
        {t("pages.restaurant.reservationButton")}
      </Button>

      <Modal
        title={"Oops!"}
        opened={showModal}
        onClose={() => setShowModal(false)}
        centered
      >
        <Center>
          <Text size="md" mb="xl">
            {t("restaurantCard.reservationTooltip")}
          </Text>
        </Center>
        <Button
          color="orange"
          variant="outline"
          size="md"
          fullWidth
          onClick={() => navigate("/login")}
        >
          {t("pages.login.signIn")}
        </Button>
      </Modal>
    </Box>
  );
}
